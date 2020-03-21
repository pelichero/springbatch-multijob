package com.medium.springbatch.multijob.jobs.interfaces;

//Nunca usar se você se gosta de testes
//import org.springframework.core.io.UrlResource;

import org.springframework.core.io.Resource;


public interface ConfigurableJob {

    Integer linesToSkip();

    Integer chunkSize();

    String tableName();

    String jobName();

    String jobStepName();

    String jobReaderName();

    String jobProcessorName();

    String jobWriterName();

    String jobLineMapperName();

    String jobReaderListenerName();

    String jobProcessorListenerName();

    String jobWriterListenerName();

    String jobStepListenerName();

    Resource csvResource();

    String[] fields();

    String sqlInsert();

    int[] includedFields();

}