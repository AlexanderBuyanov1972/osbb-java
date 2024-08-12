package com.example.osbb.security;

import com.example.osbb.controller.constants.ApiPaths;
import com.example.osbb.security.jwt.filters.JwtRequestFilter;
import com.example.osbb.security.user_details.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BeansSecurity {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
//        CsrfTokenRequestHandler requestHandler = delegate::handle;
//        http.csrf((csrf) -> csrf
//                .csrfTokenRepository(tokenRepository)
//                .csrfTokenRequestHandler(requestHandler));

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        http.cors(cors -> cors.configurationSource(request -> {
            var config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedHeaders(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedOriginPatterns(List.of("*"));
            return config;
        }));

        http.authorizeHttpRequests((requests) -> requests
                // -------------------- permitAll() ----------------------------------------------------
                .requestMatchers(HttpMethod.POST, ApiPaths.AUTH + ApiPaths.REGISTRATION).permitAll()
                .requestMatchers(HttpMethod.POST, ApiPaths.AUTH + ApiPaths.LOGIN).permitAll()
                .requestMatchers(HttpMethod.GET, ApiPaths.AUTH + ApiPaths.ACTIVATE + "/**").permitAll()
                .requestMatchers(HttpMethod.GET, ApiPaths.AUTH + ApiPaths.CHECK + "/**").permitAll()
                .requestMatchers(HttpMethod.GET, ApiPaths.AUTH + ApiPaths.REFRESH).permitAll()
                // ---------------- actuator -------------------------
                .requestMatchers(ApiPaths.ACTUATOR + "/**").permitAll()
                // ------------------- hasAnyRole("ADMIN", "USER") -------------------------------------
                .requestMatchers(HttpMethod.GET, ApiPaths.AUTH + ApiPaths.LOGOUT).hasAnyRole("ADMIN", "MANAGER")
                //--------------------------------------------------
                .requestMatchers(ApiPaths.REGISTRY + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.SELECT + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.PRINT + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.PAYMENT + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.RECORD + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.USER + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.OWNER + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.PHOTO + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.PASSPORT + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.VEHICLE + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.ADDRESS + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.RATE + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.SURVEYS + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.OWNERSHIP + "/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(ApiPaths.QUERIES + "/**").hasAnyRole("ADMIN", "MANAGER")
                .anyRequest().authenticated());

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}