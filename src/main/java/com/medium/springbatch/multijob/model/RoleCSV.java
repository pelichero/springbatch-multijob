package com.medium.springbatch.multijob.model;

import com.medium.springbatch.multijob.model.CSV;
import com.medium.springbatch.multijob.util.CompositeIdentifier;
import com.medium.springbatch.multijob.util.MultiJobAppSQLHelper;
import com.medium.springbatch.multijob.util.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

import static com.medium.springbatch.multijob.util.MultiJobAppHelper.allFieldsFor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class RoleCSV
		implements CSV {

    private String id;
    private String name;
    private String active;

    @Override
    public String sqlInsert() {
        return MultiJobAppSQLHelper.createInsertByFields(tableName(), allFieldsFor(this.getClass()).map(Field::getName).toArray(String[]::new));
    }
}
