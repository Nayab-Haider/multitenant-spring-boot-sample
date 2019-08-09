package com.sample.api.configuration;

import com.sample.api.utils.TenantContextHolder;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class HeaderBasedConfigResolver implements KeycloakConfigResolver {



    private final ConcurrentHashMap<String, KeycloakDeployment> cache = new ConcurrentHashMap<>();

    private static AdapterConfig adapterConfig;

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
        String realm = request.getHeader("realm");
        if (!cache.containsKey(realm)) {
            InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
            cache.put(realm, KeycloakDeploymentBuilder.build(is));
            TenantContextHolder.setCurrentTenant("1");
        }
        return cache.get(realm);
    }

    static void setAdapterConfig(AdapterConfig adapterConfig) {
        HeaderBasedConfigResolver.adapterConfig = adapterConfig;
    }
}