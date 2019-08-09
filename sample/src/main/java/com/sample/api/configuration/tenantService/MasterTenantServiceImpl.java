
package com.sample.api.configuration.tenantService;

import com.sample.api.configuration.tenantDomain.MasterTenant;
import com.sample.api.configuration.tenantRepository.MasterTenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterTenantServiceImpl implements MasterTenantService {

    @Autowired
    MasterTenantRepository masterTenantRepo;

    @Override
    public MasterTenant findByTenantId(String tenantId) {
        return masterTenantRepo.findByTenantId(tenantId);
    }

}
