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
    private static final String[] PUBLIC = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/doc/swagger-ui.html",
            "/doc/swagger-ui/**",
            "/webjars/**",
            "/api/registration/**",
            "/api/registration-professor/**",
            "/api/authentication",
            "/api/loadEmailForPasswordChange",
            "/api/validate-email",
            "/api/subject/all",
            "/api/app-user/changePassword",
            "/api/forgot_password/**",
            "/api/is-token-expired"
    };
    private static final String[] PROFESSOR_AUTHORITIES = {
            "/api/reservation/createUnavailable",
            "/api/professor/removeFeedback/**",
            "/api/professor-subject/edit-price"
    };
    private static final String[] STUDENT_AUTHORITIES = {
            "/api/reservation/create"
    };
    private static final String[] ADMIN_AUTHORITIES = {
            "/api/subject/edit-price",
            "/api/subject/create",
            "/api/reservation/todaySummary",
            "/api/professor-subject/reject",
            "/api/professor-subject/approve",
            "/api/professor-subject/findByStatus",
            "/api/feedback/getAllFeedbacks",
    };
    private static final String[] PROFESSOR_AND_STUDENT_AUTHORITIES = {
            "/api/reservation/cancel",
            "/api/notification/**",
            "/api/file/**",
            "/api/feedback/giveFeedback",
            "/api/comment/upload",
            "/api/app-user/update/**",
            "/api/loadedData/get-uploaded-data",
    };
    private static final String[] PROFESSOR_AND_ADMIN_AUTHORITIES = {
            "/api/reservation/getStatistics"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        // Public access
                        .requestMatchers(PUBLIC).permitAll()
                        // Role-based access control
                        .requestMatchers(PROFESSOR_AUTHORITIES).hasAuthority("PROFESSOR")
                        .requestMatchers(ADMIN_AUTHORITIES).hasAuthority("ADMIN")
                        .requestMatchers(STUDENT_AUTHORITIES).hasAuthority("STUDENT")
                        .requestMatchers(PROFESSOR_AND_ADMIN_AUTHORITIES).hasAnyAuthority("PROFESSOR", "ADMIN")
                        .requestMatchers(PROFESSOR_AND_STUDENT_AUTHORITIES).hasAnyAuthority("PROFESSOR", "STUDENT")
                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors();

        return http.build();
    }
}