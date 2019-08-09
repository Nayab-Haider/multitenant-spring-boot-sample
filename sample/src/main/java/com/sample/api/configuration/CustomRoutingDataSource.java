package com.sample.api.configuration;

import com.sample.api.utils.TenantContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();     // get request object
        if (attr != null) {
            TenantContextHolder.setCurrentTenant("1");
            String tenantId = attr.getRequest().getHeader("realm");       // find parameter from request
            return "1";
        } else {
            return "2";             // default data source
        }
    }
}
