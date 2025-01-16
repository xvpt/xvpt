package org.xvpt.website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.xvpt.website.util.KeycloakJwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .oauth2ResourceServer(
                        oauth -> oauth
                                .jwt(jwt -> jwt
                                        .jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter())
                                )
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/competition/host").hasRole("xvpt_admin")
                        .anyRequest().authenticated()
                )
                .build();
    }
}
