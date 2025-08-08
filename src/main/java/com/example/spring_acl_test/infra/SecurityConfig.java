package com.example.spring_acl_test.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())       // disable CSRF if not needed (e.g. for APIs)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()    // require authentication for all endpoints
                )
                .httpBasic(Customizer.withDefaults())  // if you want to keep httpBasic auth (optional)
                //.formLogin(form -> form.disable())
                .userDetailsService(userDetailsService);    // disable the default login form

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // case bcrypt
        // for plaintext passwords
         return NoOpPasswordEncoder.getInstance();
    }

//    @Autowired
//    private PermissionEvaluator permissionEvaluator;
//
//    @Override
//    protected MethodSecurityExpressionHandler createExpressionHandler() {
//        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
//        handler.setPermissionEvaluator(permissionEvaluator);
//        return handler;
//    }
}

