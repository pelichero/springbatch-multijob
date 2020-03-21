package com.medium.springbatch.multijob.util;

import com.medium.springbatch.multijob.model.CSV;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class StringToCSVFactory {

    private static final Logger LOG = LoggerFactory.getLogger(StringToCSVFactory.class);

    private static final String MODEL_PATH = "com.medium.springbatchtuto.model";

    public Class<? extends CSV> decideWhichCSVByLine(String line){
        try{
            return csvTypes().stream()
                    .filter(c -> c.isAnnotationPresent(CompositeIdentifier.class))
                    .filter(c -> line.equalsIgnoreCase(c.getAnnotation(CompositeIdentifier.class).value()))
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }catch (IllegalArgumentException ex) {
            LOG.debug("Line not recognized as table parameter (" + line + ") - " + ex.getMessage());
        }
        return null;
    }

    private List<Class<? extends CSV>> csvTypes() {
        Reflections reflections = new Reflections(MODEL_PATH);

        Set<Class<? extends CSV>> csvs = reflections.getSubTypesOf(CSV.class);
        return csvs.stream().collect(Collectors.toList());
    }

}
