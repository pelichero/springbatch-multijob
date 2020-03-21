package com.medium.springbatch.multijob.model;

import com.medium.springbatch.multijob.util.CompositeIdentifier;
import com.medium.springbatch.multijob.util.TableName;
import org.springframework.core.annotation.AnnotationConfigurationException;

import java.io.Serializable;

/**
 * Created by fepelichero on 17/02/2020.
 */
public interface CSV extends Serializable {

    String sqlInsert();

    default String compositeCSVIdentifier() {
        if (this.getClass().isAnnotationPresent(CompositeIdentifier.class)) {
            return this.getClass().getAnnotation(CompositeIdentifier.class).value();
        } else {
            throw new AnnotationConfigurationException("CompositeIdentifier annotation not informed!");
        }
    }

    default String tableName() {
        if (this.getClass().isAnnotationPresent(TableName.class)) {
            return this.getClass().getAnnotation(TableName.class).value();
        } else {
            throw new AnnotationConfigurationException("TableName annotation not informed!");
        }
    }

}
