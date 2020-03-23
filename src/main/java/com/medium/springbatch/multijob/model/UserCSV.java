package com.medium.springbatch.multijob.model;

import com.medium.springbatch.multijob.util.MultiJobAppHelper;
import com.medium.springbatch.multijob.util.MultiJobAppSQLHelper;
import com.medium.springbatch.multijob.util.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserCSV implements CSV {

    private String id;
    private String name;
    private String role_id;
    private String active;

    @Override
    public String sqlInsert() {
        return MultiJobAppSQLHelper.createInsertByFields(tableName(), MultiJobAppHelper.allFieldsFor(this.getClass()).map(Field::getName).toArray(String[]::new));
    }
}
