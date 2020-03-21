package com.medium.springbatch.multijob.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

import java.io.FileNotFoundException;

import static java.text.MessageFormat.format;


public class DefaultSkipPolicy
		implements SkipPolicy {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSkipPolicy.class);

    private static final String ERROR_MESSAGE = "An error occured while processing the {0} line of the file. Below was the faulty input.\n {1} \n";
    public static final int SKIP_MIN_COUNT = 5;

    @Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
	    if (exception instanceof FileNotFoundException) {
	        return false;
	    } else if (exception instanceof FlatFileParseException && skipCount <= SKIP_MIN_COUNT) {
	        FlatFileParseException ffpe = (FlatFileParseException) exception;
                String  errorMessage = format(ERROR_MESSAGE, ffpe.getLineNumber(), ffpe.getInput());
	        LOG.error("{}", errorMessage);
	        return true;
	    } else {
	        return false;
	    }
	}
}