package com.medium.springbatch.multijob.components;

import com.medium.springbatch.multijob.util.TableName;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import java.util.List;
import java.util.stream.Collectors;

public class JdbcBatchFilteredItemWriter<W> extends JdbcBatchItemWriter<W> {
    private String tableName;

    @Override
    public void write(List items) throws Exception {
        items = (List)items.stream().filter(item -> {
            return item.getClass().isAnnotationPresent(TableName.class) &&
                    item.getClass().getAnnotation(TableName.class).value().equalsIgnoreCase(this.tableName);
        }).collect(Collectors.toList());
        super.write(items);
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}