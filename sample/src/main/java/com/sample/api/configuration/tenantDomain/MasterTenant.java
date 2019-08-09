package com.sample.api.configuration.tenantDomain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "master_tenant")
public class MasterTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(max = 30)
    @Column(name = "tenant_id")
    private String tenantId;

    @Size(max = 256)
    @Column(name = "url")
    private String url;

    @Size(max = 30)
    @Column(name = "tenant_name")
    private String tenantName;

    @Size(max = 30)
    @Column(name = "username")
    private String username;

    @Size(max = 30)
    @Column(name = "password")
    private String password;

    @Version
    private int version = 0;

}
