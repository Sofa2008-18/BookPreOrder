
package com.bookstore.bookpreorder.config;

import com.bookstore.bookpreorder.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Регистрация и авторизация
                        .requestMatchers("/api/auth/**").permitAll()

                        // Swagger и OpenAPI
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Публичный просмотр книг
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/books/**"
                        ).permitAll()

                        // Публичный просмотр категорий
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/categories/**"
                        ).permitAll()

                        // Административные операции
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Управление книгами
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        // Управление категориями
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/categories/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/categories/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/categories/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/categories/**"
                        ).hasRole("ADMIN")

                        // Заказы и предзаказы
                        .requestMatchers("/api/orders/**").authenticated()
                        .requestMatchers("/api/preorders/**").authenticated()

                        // Остальные запросы требуют авторизации
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    // Шифрование паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
