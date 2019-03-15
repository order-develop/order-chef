package com.example.chef.datasource;



/**
 * description: TODO
 * create: 2019/1/29 13:49
 *
 * @author NieMingXin
 */

public class DataSourceContextHolder {
    private static final String DEFAULT_DATASOURCE = "SlAVE_DATASOURCE";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDataSource(String dbType) {
        System.out.println("切换到" + dbType + "数据源");
        contextHolder.set(dbType);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}