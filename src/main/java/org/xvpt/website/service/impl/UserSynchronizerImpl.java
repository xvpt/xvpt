package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.User;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.service.UserMapper;
import org.xvpt.website.service.UserSynchronizer;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizerImpl implements UserSynchronizer {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void synchronizeWithIdp(@NotNull JwtAuthenticationToken authentication) {
        log.info("Synchronizing user with idp");
        Jwt jwt = authentication.getToken();
        getUserEmail(jwt).ifPresent(userEmail -> {
            log.info("Synchronizing user having email {}", userEmail);
            User user = userMapper.fromTokenAttributes(jwt.getClaims());
            user.setRoles(authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(authority -> authority.startsWith("ROLE_"))
                    .map(authority -> authority.substring("ROLE_".length()))
                    .toList());
            userRepository.save(user);
        });

    }

    private Optional<String> getUserEmail(@NotNull Jwt token) {
        Map<String, Object> attributes = token.getClaims();
        if (attributes.containsKey("email")) {
            return Optional.of(attributes.get("email").toString());
        }
        return Optional.empty();

    }
}