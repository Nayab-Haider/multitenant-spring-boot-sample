
package com.sample.api.configuration.tenantService;

import com.sample.api.configuration.tenantDomain.MasterTenant;
import org.springframework.data.repository.query.Param;


public interface MasterTenantService {

    MasterTenant findByTenantId(@Param("tenantId") String tenantId);
}
