package com.medium.springbatch.multijob.util;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MultiJobAppSQLHelper {

    public static final String INSERT_INTO     = "INSERT INTO ";
    public static final String PARENTHESES_    = " ( ";
    public static final String _PARENTHESES    = " ) ";
    public static final String VALUES          = "VALUES";
    public static final String SQL_VARS        = ":";
    public static final String COMMA           = ",";
    public static final int    START_INCLUSIVE = 0;

    public static String createInsertByFields(String table, String[] fields){
        return INSERT_INTO
                .concat(table)
                .concat(PARENTHESES_)
                .concat(String.join(COMMA, fields))
                .concat(_PARENTHESES)
                .concat(VALUES)
                .concat(PARENTHESES_)
                .concat(Arrays.stream(fields).map(SQL_VARS::concat).collect(Collectors.joining(COMMA)))
                .concat(_PARENTHESES);
    }

    public static int[] createIncludedFields(String[] fields){
        int[] includedFields = new int[fields.length];
        IntStream.range(START_INCLUSIVE, fields.length).forEach(i -> includedFields[i] = i);
        return includedFields;
    }

}
