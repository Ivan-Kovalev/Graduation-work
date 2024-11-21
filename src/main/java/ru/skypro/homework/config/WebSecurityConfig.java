package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.skypro.homework.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности для приложения.
 * Настроены параметры аутентификации и авторизации для различных URL-адресов.
 * Этот класс обеспечивает:
 * Настройку аутентификации с использованием {@link DaoAuthenticationProvider},
 * который работает с {@link UserDetailsService} и {@link PasswordEncoder} для проверки данных пользователя.
 * Обеспечение доступа к ресурсам Swagger и публичным эндпоинтам (например, /login и /register) без аутентификации.
 * Настройку CORS и отключение CSRF для упрощения конфигурации API.
 * Параметры авторизации для ограниченных ресурсов: доступ только для аутентифицированных пользователей для некоторых URL.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/images/ad/preview/**"
    };

    /**
     * Конфигурация {@link DaoAuthenticationProvider} для аутентификации пользователей.
     * Используется {@link UserDetailsService} и {@link PasswordEncoder} для проверки данных пользователя.
     *
     * @return настроенный {@link DaoAuthenticationProvider}.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Конфигурация {@link UserDetailsService} для работы с пользователями через {@link UserRepository}.
     *
     * @return настроенный {@link UserDetailsService}, который использует {@link UserRepository}.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new PersonDetailService(userRepository);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*");
            }
        };
    }

    /**
     * Конфигурация фильтрации запросов с использованием {@link HttpSecurity}.
     * Настроены правила доступа для различных URL-адресов, а также параметры CORS и отключение CSRF.
     *
     * @param http {@link HttpSecurity}, для настройки фильтров безопасности.
     * @return настроенная {@link SecurityFilterChain}.
     * @throws Exception если произошла ошибка настройки безопасности.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }
    /**
     * Конфигурация {@link PasswordEncoder} с использованием алгоритма BCrypt.
     *
     * @return настроенный {@link PasswordEncoder} для шифрования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

}
