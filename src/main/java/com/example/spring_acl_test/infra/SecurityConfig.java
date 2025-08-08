package com.example.spring_acl_test.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())       // disable CSRF if not needed (e.g. for APIs)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()        // allow all requests without authentication
                )
                .httpBasic(Customizer.withDefaults())  // if you want to keep httpBasic auth (optional)
                .formLogin(form -> form.disable());    // disable the default login form

        return http.build();
    }
}

