package com.example.osbb.security;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.security.jwt.filters.AuthenticationFilter;
import com.example.osbb.security.jwt.filters.AuthorizationFilter;
import com.example.osbb.security.jwt.filters.RefreshFilter;
import com.example.osbb.security.user_details.CustomUserDetailsService;
import com.example.osbb.service.CookieService;
import com.example.osbb.service.TokenService;
import com.example.osbb.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;


import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;
    private final CookieService cookieService;
    private final UserService userService;
    private final String[] paths = new String[]{
            ApiConstants.PRINT, ApiConstants.PRINT + ApiConstants.SS,
            ApiConstants.PAYMENT, ApiConstants.PAYMENT + ApiConstants.SS,
            ApiConstants.RECORD, ApiConstants.RECORD + ApiConstants.SS,
            ApiConstants.SHARE, ApiConstants.SHARE + ApiConstants.SS,
            ApiConstants.USER, ApiConstants.USER + ApiConstants.SS,
            ApiConstants.OWNER, ApiConstants.OWNER + ApiConstants.SS,
            ApiConstants.PHOTO, ApiConstants.PHOTO + ApiConstants.SS,
            ApiConstants.PASSPORT, ApiConstants.PASSPORT + ApiConstants.SS,
            ApiConstants.VEHICLE, ApiConstants.VEHICLE + ApiConstants.SS,
            ApiConstants.ADDRESS, ApiConstants.ADDRESS + ApiConstants.SS,
            ApiConstants.RATE, ApiConstants.RATE + ApiConstants.SS,
            ApiConstants.QUESTIONNAIRES, ApiConstants.QUESTIONNAIRES + ApiConstants.SS,
            ApiConstants.OWNERSHIP, ApiConstants.OWNERSHIP + ApiConstants.SS,
            ApiConstants.REGISTRY, ApiConstants.REGISTRY + ApiConstants.SS,
            ApiConstants.SELECT, ApiConstants.SELECT + ApiConstants.SS,
    };
    private final String[] publicPath = new String[]{ApiConstants.REGISTRATION, ApiConstants.LOGIN};

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             TokenService tokenService,
                             CookieService cookieService,
                             UserService userService) {
        this.userService = userService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
        this.cookieService = cookieService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
//        CsrfTokenRequestHandler requestHandler = delegate::handle;
//        http.csrf((csrf) -> csrf
//                .csrfTokenRepository(tokenRepository)
//                .csrfTokenRequestHandler(requestHandler));

        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
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

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.exceptionHandling().authenticationEntryPoint(unauthorizedResponse());
        http.authorizeHttpRequests((requests) -> requests
                // -------------------- permitAll() ----------------------------------------------------
                .requestMatchers(HttpMethod.POST, ApiConstants.REGISTRATION).permitAll()
                .requestMatchers(HttpMethod.POST, ApiConstants.LOGIN).permitAll()
                .requestMatchers(HttpMethod.GET, ApiConstants.ACTIVATE + "/**").permitAll()
                .requestMatchers(HttpMethod.GET, ApiConstants.CHECK + "/**").permitAll()
                .requestMatchers(HttpMethod.GET, ApiConstants.REFRESH).permitAll()
                // ------------------- hasAnyRole("ADMIN", "USER") -------------------------------------
                .requestMatchers(HttpMethod.GET, ApiConstants.LOGOUT).hasAnyRole("ADMIN", "USER")
                //-------------------------------------------------
                .requestMatchers(HttpMethod.POST, paths).permitAll()
                .requestMatchers(HttpMethod.PUT, paths).permitAll()
                .requestMatchers(HttpMethod.GET, paths).permitAll()
                .requestMatchers(HttpMethod.DELETE, paths).permitAll()
                .anyRequest().authenticated());

        // filters
        http.addFilter(new AuthenticationFilter(authenticationManagerBean(), userService, tokenService, cookieService));
        http.addFilter(new RefreshFilter(authenticationManagerBean(), tokenService, cookieService, userService));
        http.addFilterAfter(new AuthorizationFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private AuthenticationEntryPoint unauthorizedResponse() {
        return (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Bean
    AuthenticationManager authenticationManagerBean() {
        return authentication -> {
            String username = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";

            UserDetails user = customUserDetailsService.loadUserByUsername(username);

            if (!passwordEncoder().matches(password, user.getPassword())) {
                throw new BadCredentialsException("Bad credentials");
            }

            if (!user.isEnabled()) {
                throw new DisabledException("User account is not active");
            }

            return new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        };
    }



    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

}