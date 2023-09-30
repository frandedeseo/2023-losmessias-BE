package com.losmessias.leherer.security.config;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.role.AppUserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.losmessias.leherer.service.AppUserService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final AppUserService AppUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserRepository appUserRepository;

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
                    authorize.requestMatchers("/teacher").hasAuthority("ADMIN");
                    authorize.requestMatchers("/**").permitAll();
                    authorize.anyRequest().authenticated();
                }).formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .permitAll()
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                                Authentication authentication) throws IOException, ServletException {
                                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                                Boolean isStudent = false;
                                Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
                                for (GrantedAuthority rol : roles) {
                                    isStudent = rol.getAuthority().equals("STUDENT");
                                }
                                String username = ((UserDetails) authentication.getPrincipal()).getUsername();
                                AppUser appUser = appUserRepository.findByEmail(username);
                                Long id = appUser.getId();

                                if (isStudent) {
                                    response.sendRedirect("http://localhost:3000/student-landing?id=" + id);
                                } else {
                                    response.sendRedirect("http://localhost:3000/professor-landing?id=" + id);
                                }
                            }
                        })
                );
        return http.build();
    }
}
