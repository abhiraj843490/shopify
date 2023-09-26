package com.appsdeveloperblog.shopify.clients.security.service;

import com.appsdeveloperblog.shopify.clients.config.ShopifyDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthFilter jwtAuthFilter;
    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopifyDetailsService();
    }

    public static final String[] PUBLIC_URLS = {
            "/actuator/**"
            ,"/v2/api-docs",
            "/v3/api-docs","/v3/api-docs/**","/swagger-resources",
            "/swagger-resources/**"
            ,"/swagger-ui/**","/swagger-ui.html","/","/api/auth","/success/**","/login","/products/delete","/products/update","/products/create" ,"/customer/**","/call_back/fulfillment_order_notification"
    };
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder=
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());

        return http.csrf().disable().cors().and().authorizeHttpRequests()
                .requestMatchers(PUBLIC_URLS).permitAll()
                .and().authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
