package com.losmessias.leherer.security.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.losmessias.leherer.service.AppUserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final AppUserService AppUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(AppUserService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("api/v1/registration/**").permitAll();
                            authorize.requestMatchers("/student").hasAuthority("STUDENT");
                            authorize.requestMatchers("/teacher").hasAuthority("TEACHER");
                            authorize.anyRequest().authenticated();
                }).formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("http://localhost:3000/landing-page")
                );
        return http.build();
    }
}
