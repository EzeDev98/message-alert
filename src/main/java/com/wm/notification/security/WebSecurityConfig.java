package com.wm.notification.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    public static final String ADMIN = "admin";

    public static final String GENERAL = "general";

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,  "/api/v1/notification/read/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/notification/{receiver}", "/api/v1/notification/all",
                                "/api/v1/notification/user").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> {
            web.ignoring().requestMatchers(
                    HttpMethod.POST,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.GET,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.DELETE,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                    HttpMethod.PUT,
                    "/public/**"
            );
            web.ignoring().requestMatchers(
                            HttpMethod.OPTIONS,
                            "/**"
                    )
                    .requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger-ui/**",
                            "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/api-docs/**");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}