package com.if2c.harald.dao;


public class DataSourceContextHolder {
    public final static String DATA_SOURCE_TARGET = "targetDataSource";
    public final static String DATA_SOURCE_ONLINE = "onlineDataSource";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return ((String) contextHolder.get());
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}