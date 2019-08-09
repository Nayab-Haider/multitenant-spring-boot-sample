

package com.sample.api.configuration.multitenancyConfig;

import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken
        extends KeycloakAuthenticationToken {

    private static final long serialVersionUID = 1L;

    /**
     * The tenant i.e. database identifier
     */
    private String tenant;

    /**
     * @param principal
     * @param credentials
     * @param tenant
     */
    public CustomAuthenticationToken(Object principal, Object credentials,
                                     String tenant) {
        super((KeycloakAccount) principal, true);
        this.tenant = tenant;
        super.setAuthenticated(false);
    }

    /**
     * @param principal
     * @param credentials
     * @param tenant
     * @param authorities
     */
    public CustomAuthenticationToken(Object principal, Object credentials,
                                     String tenant, Collection<? extends GrantedAuthority> authorities) {
        super((KeycloakAccount) principal, true, authorities);
        this.tenant = tenant;
        super.setAuthenticated(true); // must use super, as we override
    }

    public String getTenant() {
        return this.tenant;
    }
}