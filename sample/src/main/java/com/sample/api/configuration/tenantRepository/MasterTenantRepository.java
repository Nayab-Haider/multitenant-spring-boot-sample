package com.sample.api.configuration.tenantRepository;

import com.sample.api.configuration.tenantDomain.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {

    MasterTenant findByTenantId(String tenantId);
}
