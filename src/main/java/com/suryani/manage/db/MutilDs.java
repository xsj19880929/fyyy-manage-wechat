package com.suryani.manage.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MutilDs extends AbstractRoutingDataSource {
    private static Log log=LogFactory.getLog(MutilDs.class);
    @Override
    protected Object determineCurrentLookupKey() {
        Object key=DsContextHolder.getKey();
        log.info("use datasource of "+key);
        return key;
    }
}
