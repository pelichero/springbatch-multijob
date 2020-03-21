package com.medium.springbatch.multijob.jobs.interfaces;

import org.springframework.batch.core.*;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;

import java.lang.reflect.ParameterizedType;


public interface StructuredJob<R,W> extends ConfigurableJob {

    org.springframework.batch.core.Job job() ;

    Step step();

    ItemReader<R> reader();

    LineMapper<R> lineMapper();

    ItemProcessor<R, W> processor();

    ItemWriter<W> writer();

    ItemReadListener<R> readerListener();

    ItemProcessListener<R,W> processorListener();

    ItemWriteListener<W> writerListener();

    StepExecutionListenerSupport stepListener();

    default Class<R> getCSVClass(){
        try {
            return (Class<R>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Não foi possível resolver o tipo genérico da classe: " + this.getClass().getName());
        }
    }

}
