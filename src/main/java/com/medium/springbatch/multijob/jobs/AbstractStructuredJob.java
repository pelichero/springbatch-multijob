package com.medium.springbatch.multijob.jobs;

import com.medium.springbatch.multijob.components.JdbcBatchFilteredItemWriter;
import com.medium.springbatch.multijob.jobs.interfaces.StructuredJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;

import static com.medium.springbatch.multijob.util.MultiJobAppConstants.Configuration.*;
import static com.medium.springbatch.multijob.util.MultiJobAppHelper.allFieldsFor;
import static com.medium.springbatch.multijob.util.MultiJobAppSQLHelper.createIncludedFields;
import static com.medium.springbatch.multijob.util.MultiJobAppSQLHelper.createInsertByFields;
import static java.text.MessageFormat.format;


@PropertySource("classpath:job-configurations.properties")
public abstract class AbstractStructuredJob<R,W> implements StructuredJob<R,W> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractStructuredJob.class);

    protected static final String JOB_CONTEXT_DATA_KEY = "jobContextKey";

    private Integer linesToSkip;

    private Integer chunkSize;

    private String tableName;

    private String jobName;

    protected Resource csvFileLocation;

    public AbstractStructuredJob(Integer linesToSkip, Integer chunkSize, String tableName, String jobName, Resource csvFileLocation) {
        this.linesToSkip = linesToSkip;
        this.chunkSize = chunkSize;
        this.tableName = tableName;
        this.jobName = jobName;
        this.csvFileLocation = csvFileLocation;
    }

    @Autowired
    protected JobBuilderFactory jobBuilderFactory;

    @Autowired
    protected StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier(ETL_DATA_SOURCE_QUALIFIER)
    protected DataSource dataSource;

    @Autowired
    @Qualifier(SKIPPER_QUALIFIER)
    protected SkipPolicy defaultSkipPolicy;

    @Override
    public org.springframework.batch.core.Job job() {
        return jobBuilderFactory
                        .get(jobName())
                        .incrementer(new RunIdIncrementer())
                        .start(step())
                        .build();
    }

    @Override
    public Step step() {
        return stepBuilderFactory
                        .get(jobName())
                        .<R, W>chunk(chunkSize())
                        .reader(reader())
                        .faultTolerant().skipPolicy(defaultSkipPolicy)
                        .listener(readerListener())
                        .processor(processor())
                        .listener(processorListener())
                        .writer(writer())
                        .listener(writerListener())
                        .listener(stepListener())
                        .build();
    }

    @Override
    public ItemReader<R> reader() {
        FlatFileItemReader<R> itemReader = new FlatFileItemReader<>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(linesToSkip());
        itemReader.setResource(csvResource());
        return itemReader;
    }

    @Override
    public ItemProcessor<R, W> processor() {
        return r -> {
            LOG.info(r.toString());
            try {
                return (W) r;
            } catch (ClassCastException cEx) {
                LOG.error(cEx.getMessage());
                return null;
            }
        };
    }

    @Override
    public ItemWriter<W> writer() {
        Class<R> csv = getCSVClass();
        JdbcBatchFilteredItemWriter<W> itemWriter = new JdbcBatchFilteredItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setTableName(tableName);
        itemWriter.setSql(sqlInsert());
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();
        return itemWriter;
    }

    @Override
    public LineMapper<R> lineMapper() {
        DefaultLineMapper<R> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        BeanWrapperFieldSetMapper<R> fieldSetMapper = new BeanWrapperFieldSetMapper<>();

        lineTokenizer.setNames(fields());
        lineTokenizer.setIncludedFields(includedFields());
        fieldSetMapper.setTargetType(getCSVClass());
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Override
    public StepExecutionListenerSupport stepListener(){
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        executionContextPromotionListener.setKeys(new String[] {JOB_CONTEXT_DATA_KEY});
        return executionContextPromotionListener;
    }

    @Override
    public ItemReadListener<R> readerListener() {
        return new ItemReadListener<R>() {

            @Override
            public void beforeRead() {
            }

            @Override
            public void afterRead(R r) {
            }

            @Override
            protected void finalize() throws Throwable {
                LOG.info(" =========================================");
                LOG.info(format(" FINALIZING READING {0} ", jobReaderName()));
                LOG.info(" ========================================= ");
                super.finalize();
            }

            @Override
            public void onReadError(Exception e) {
                LOG.error(" =========================================");
                LOG.error(format(" ERROR READING {0} ", jobReaderName()));
                LOG.error(e.getMessage());
                LOG.error(" ========================================= ");
            }
        };
    }

    @Override
    public ItemProcessListener<R,W> processorListener() {
        return new ItemProcessListener<R,W> () {

            @Override
            public void beforeProcess(R r) {
            }

            @Override
            public void afterProcess(R r, W w) {
            }

            @Override
            protected void finalize() throws Throwable {
                LOG.info(" =========================================");
                LOG.info(format(" FINALIZING PROCESSING {0} ", jobProcessorName()));
                LOG.info(" ========================================= ");
                super.finalize();
            }

            @Override
            public void onProcessError(R r, Exception e) {
                LOG.error(" =========================================");
                LOG.error(format(" ERROR PROCESSING {0} ", jobProcessorName()));
                LOG.error(e.getMessage());
                LOG.error(" ========================================= ");
            }
        };
    }

    @Override
    public ItemWriteListener<W> writerListener() {
        return new ItemWriteListener<W>() {
            @Override
            public void beforeWrite(List<? extends W> list) {
                LOG.info(" =========================================");
                LOG.info(format(" INITIALIZING WRITING {0} ", jobWriterName()));
                LOG.info(" =========================================");
            }

            @Override
            public void afterWrite(List<? extends W> list) {
                LOG.info(" =========================================");
                LOG.info(format(" FINALIZING WRITING {0} ", jobWriterName()));
                LOG.info(" ========================================= ");
            }

            @Override
            public void onWriteError(Exception e, List<? extends W> list) {
                LOG.error(" =========================================");
                LOG.error(format(" ERROR WRITING {0} ", jobWriterName()));
                LOG.error(e.getMessage());
                LOG.error(" ========================================= ");
            }
        };
    }

    @Override public Integer linesToSkip() {
        return linesToSkip;
    }

    @Override public Integer chunkSize() {
        return chunkSize;
    }

    @Override public String tableName() {
        return tableName;
    }

    @Override public String jobName() {
        return jobName;
    }

    @Override public String jobStepName() {
        return jobName.concat("Step");
    }

    @Override public String jobReaderName() {
        return jobName.concat(QUALIFIER_SUFFIX_READER);
    }

    @Override public String jobProcessorName() {
        return jobName.concat(QUALIFIER_SUFFIX_PROCESSOR);
    }

    @Override public String jobWriterName() {
        return jobName.concat(QUALIFIER_SUFFIX_WRITER);
    }

    @Override public String jobLineMapperName() {
        return jobName.concat("LineMapper");
    }

    @Override
    public String jobReaderListenerName() {
        return jobName.concat(QUALIFIER_SUFFIX_READER_LISTENER);
    }

    @Override
    public String jobProcessorListenerName() {
        return jobName.concat(QUALIFIER_SUFFIX_PROCESSOR_LISTENER);
    }

    @Override
    public String jobWriterListenerName() {
        return jobName.concat(QUALIFIER_SUFFIX_WRITER_LISTENER);
    }

    @Override
    public String jobStepListenerName() {
        return jobName.concat(QUALIFIER_SUFFIX_STEP_LISTENER);
    }

    @Override public Resource csvResource() {
        return csvFileLocation;
    }

    @Override
    public String[] fields() {
        return allFieldsFor(getCSVClass()).map(Field::getName).toArray(String[]::new);
    }

    @Override
    public String sqlInsert() {
        return createInsertByFields(tableName, fields());
    }

    @Override
    public int[] includedFields() {
        return createIncludedFields(fields());
    }

}
