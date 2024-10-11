package com.losmessias.leherer.security.config;

import com.losmessias.leherer.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import com.losmessias.leherer.domain.enumeration.AppUserRole;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private static final String[] WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/doc/swagger-ui.html",
            "/doc/swagger-ui/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/api/registration/**").permitAll()
                                .requestMatchers("/api/registration-professor/**").permitAll()
                                .requestMatchers("/api/authentication").permitAll()
                                .requestMatchers("/api/loadEmailForPasswordChange").permitAll()
                                .requestMatchers("/api/validate-email").permitAll()
                                .requestMatchers("/api/calendar/**").permitAll()
                                .requestMatchers("/api/subject/all").permitAll()
                                .requestMatchers("/oauth2callback").permitAll()
                                .requestMatchers("/api/app-user/changePassword").permitAll()
                                .requestMatchers("/api/forgot_password/**").permitAll()
                                .requestMatchers("/api/is-token-expired").permitAll()
                                .requestMatchers(WHITELIST).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors();
        return http.build();
    }
}
