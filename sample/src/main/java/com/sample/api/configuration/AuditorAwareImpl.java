package com.sample.api.configuration;

import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private AccessToken accessToken;

    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = accessToken.getPreferredUsername();
        return Optional.of(userId != null ? userId : "");
    }

}