package com.example.chef.datasource;

/**
 * description: TODO
 * create: 2019/1/29 15:25
 *
 * @author NieMingXin
 */

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}
