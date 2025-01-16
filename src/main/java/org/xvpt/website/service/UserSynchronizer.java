package org.xvpt.website.service;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public interface UserSynchronizer {
    void synchronizeWithIdp(JwtAuthenticationToken token);
}
