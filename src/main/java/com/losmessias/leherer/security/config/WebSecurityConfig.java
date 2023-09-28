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
                        .loginProcessingUrl("/teacher")
                );
        return http.build();
    }
}

/*
package com.losmessias.leherer.security.config;

import com.losmessias.leherer.service.AppUserService;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
class SpringSecurityConfig {

    @Bean
    UserDetailsService jdbcUserDetailsManager(DataSource dataSource,
                                              @Value("${security-jdbc.user}") String usersByUsernameQuery,
                                              @Value("${security.jdbc-authorities}") String authoritiesByUsernameQuery) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(usersByUsernameQuery);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(authoritiesByUsernameQuery);
        return jdbcUserDetailsManager;
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeRequests(auth -> {
                    auth.requestMatchers().permitAll();
                    auth.requestMatchers().permitAll();
                            auth.anyRequest().authenticated();
                });

        return http.build();
    }

}

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeRequests(auth -> {
                    auth.requestMatchers("registration").permitAll();
                    auth.requestMatchers("apilogin).permitAll();
                    auth.anyRequest().authenticated();
                });

        return http.build();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeRequests(auth -> {
                    auth.requestMatchers("/").permitAll();
                    auth.requestMatchers("/user").hasRole("USER");
                    auth.requestMatchers("/admin").hasRole("ADMIN");

                })
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

 */
