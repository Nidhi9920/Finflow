package com.nidhi.finflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //disable csrf(cross-site request forgery)
//        http.csrf(csrf -> csrf.disable()).
//                authorizeHttpRequests(auth ->
//                        auth.requestMatchers("/health",
//                                "/api/users", "api/users/**")
//                                .permitAll().anyRequest().authenticated());
//        return http.build();
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .permitAll()
                ).build();
    }
}
