package fontys.s3.uplifted.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@Profile("!test")
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private static final String ROLE_TEACHER = "TEACHER";
    private static final String ROLE_STUDENT = "STUDENT";

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("http://localhost:*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // WebSocket and SockJS (connect/info/fallbacks)
                        .requestMatchers("/ws/**", "/websocket/**", "/topic/**", "/app/**", "/info", "/error").permitAll()

                        // Test WebSocket endpoint
                        .requestMatchers(HttpMethod.POST, "/api/notifications/test").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/quizzes/submit")
                        .hasAnyRole(ROLE_STUDENT, ROLE_TEACHER)

                        // Public API
                        .requestMatchers("/api/auth/**", "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/notifications/send").permitAll()

                        // CORS Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // File downloads (authenticated)
                        .requestMatchers(HttpMethod.GET, "/api/files/*/download").authenticated()

                        // Role-specific access
                        .requestMatchers(HttpMethod.POST, "/api/courses/create").hasRole(ROLE_TEACHER)
                        .requestMatchers(HttpMethod.PUT, "/api/courses/update/**").hasRole(ROLE_TEACHER)
                        .requestMatchers(HttpMethod.DELETE, "/api/courses/delete/**").hasRole(ROLE_TEACHER)
                        .requestMatchers("/api/stats/**").hasRole(ROLE_TEACHER)

                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole(ROLE_TEACHER, ROLE_STUDENT)
                        .requestMatchers("/api/enrollments/**").hasRole(ROLE_STUDENT)

                        // Default catch-all
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
