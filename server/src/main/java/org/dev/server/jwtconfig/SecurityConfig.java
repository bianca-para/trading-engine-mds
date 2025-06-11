package org.dev.server.jwtconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@Profile("!test")
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final UnAuthorizedUserAuthenticationEntryPoint authenticationEntryPoint;
    private final SecurityFilter secFilter;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            BCryptPasswordEncoder bCryptEncoder,
            UnAuthorizedUserAuthenticationEntryPoint authenticationEntryPoint,
            SecurityFilter secFilter
    ) {
        this.userDetailsService = userDetailsService;
        this.bCryptEncoder = bCryptEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.secFilter = secFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()               // enables CORS support using corsConfigurationSource()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                )
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(secFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptEncoder);
        return authProvider;
    }

    /**
     * Defines a CorsConfigurationSource bean that:
     *  - allows ANY origin (use allowedOriginPatterns to support credentials)
     *  - allows all standard HTTP methods
     *  - allows any headers
     *  - supports cookies/auth headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
