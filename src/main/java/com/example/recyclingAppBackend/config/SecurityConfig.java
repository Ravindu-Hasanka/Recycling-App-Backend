package com.example.recyclingAppBackend.config;

import com.example.recyclingAppBackend.security.jwt.AuthTokenFilter;
import com.example.recyclingAppBackend.security.UserDetailsServiceImpl;
import com.example.recyclingAppBackend.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/stories/**", "/api/badges/**", "/api/items/**").permitAll()
                        .requestMatchers("/api/categories/view").permitAll() // Changed to permitAll to match design

                        // --- Role-Specific Endpoints ---
                        // Student-specific progress tracking
                        .requestMatchers("/api/progress/**").hasRole("STUDENT")

                        // Admin endpoints for content management
                        .requestMatchers("/api/categories/**", "/api/stories/**", "/api/badges/**", "/api/items/**").hasRole("ADMIN")

                        // Teacher-specific endpoints
                        .requestMatchers("/api/materials/**").hasRole("TEACHER")

                        // --- General Authenticated Endpoints ---
                        .requestMatchers("/api/paths/**").authenticated()

                        // Profile endpoints for any logged-in user
                        .requestMatchers("/api/profile/**").authenticated()

                        // Fallback for any other request
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}