package com.medium.springbatch.multijob.components;

import com.medium.springbatch.multijob.jobs.interfaces.StructuredJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static com.medium.springbatch.multijob.util.MultiJobAppConstants.Configuration.MULTI_JOB_APP_JOB_BEAN_REGISTER_QUALIFIER;
import static java.text.MessageFormat.format;

@Component(MULTI_JOB_APP_JOB_BEAN_REGISTER_QUALIFIER)
public class BeanRegisterComponent
		implements BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(BeanRegisterComponent.class);

    @Autowired
    private BeanFactory beanFactory;

    public BeanRegisterComponent() {
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public org.springframework.batch.core.Job registerJob(StructuredJob<?,?> multiStructuredJob) {

        LOG.info(" =========================================");
        LOG.info(format(" INITIALIZING REGISTERING {0} ", multiStructuredJob.jobName()));
        LOG.info(" =========================================");

        this.registerStep(multiStructuredJob);
        this.registerReader(multiStructuredJob);
        this.registerProcessor(multiStructuredJob);
        this.registerWriter(multiStructuredJob);
        this.registerLineMapper(multiStructuredJob);
        this.registerReaderListener(multiStructuredJob);
        this.registerProcessorListener(multiStructuredJob);
        this.registerWriterListener(multiStructuredJob);
        this.registerStepListener(multiStructuredJob);
        org.springframework.batch.core.Job job = this.registerInnerJob(multiStructuredJob);

        LOG.info(" =========================================");
        LOG.info(format(" FINALIZING REGISTERING {0} ", multiStructuredJob.jobName()));
        LOG.info(" =========================================");

        return job;
    }

    private void registerReaderListener(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobReaderListenerName()));
        String jobReaderListenerName = multiStructuredJob.jobReaderListenerName();
        ItemReadListener<?> itemReadListener = multiStructuredJob.readerListener();
        configurableBeanFactory.registerSingleton(jobReaderListenerName, itemReadListener);
    }

    private void registerProcessorListener(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobProcessorListenerName()));
        String jobProcessorListenerName = multiStructuredJob.jobProcessorListenerName();
        ItemProcessListener<?,?> itemProcessListener = multiStructuredJob.processorListener();
        configurableBeanFactory.registerSingleton(jobProcessorListenerName, itemProcessListener);
    }

    private void registerWriterListener(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobWriterListenerName()));
        String jobWriterListenerName = multiStructuredJob.jobWriterListenerName();
        ItemWriteListener<?> itemWriteListener = multiStructuredJob.writerListener();
        configurableBeanFactory.registerSingleton(jobWriterListenerName, itemWriteListener);
    }

    private org.springframework.batch.core.Job registerInnerJob(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        String jobName = multiStructuredJob.jobName();
        org.springframework.batch.core.Job job = multiStructuredJob.job();
        configurableBeanFactory.registerSingleton(jobName, job);
        return job;
    }

    private void registerStepListener(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobStepListenerName()));
        String jobStepListenerName = multiStructuredJob.jobStepListenerName();
        StepExecutionListenerSupport stepListener = multiStructuredJob.stepListener();
        configurableBeanFactory.registerSingleton(jobStepListenerName, stepListener);
    }

    private void registerLineMapper(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobLineMapperName()));
        String jobLineMapperName = multiStructuredJob.jobLineMapperName();
        LineMapper<?> lineMapper = multiStructuredJob.lineMapper();
        configurableBeanFactory.registerSingleton(jobLineMapperName, lineMapper);
    }

    private void registerWriter(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobWriterName()));

        ItemWriter<?> writer = multiStructuredJob.writer();
        String jobWriterName = multiStructuredJob.jobWriterName();
        configurableBeanFactory.registerSingleton(jobWriterName, writer);
    }

    private void registerProcessor(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobProcessorName()));
        String jobProcessorName = multiStructuredJob.jobProcessorName();
        ItemProcessor<?,?> processor = multiStructuredJob.processor();
        configurableBeanFactory.registerSingleton(jobProcessorName, processor);
    }

    private void registerReader(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobReaderName()));
        String jobReaderName = multiStructuredJob.jobReaderName();
        ItemReader<?> reader = multiStructuredJob.reader();
        configurableBeanFactory.registerSingleton(jobReaderName, reader);
    }

    private void registerStep(StructuredJob<?,?> multiStructuredJob) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)this.beanFactory;
        LOG.info(MessageFormat.format("Registering {0} ", multiStructuredJob.jobStepName()));
        String jobStepName = multiStructuredJob.jobStepName();
        Step step = multiStructuredJob.step();
        configurableBeanFactory.registerSingleton(jobStepName, step);
    }
}
