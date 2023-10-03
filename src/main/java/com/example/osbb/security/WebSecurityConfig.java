package com.example.osbb.security;

import com.example.osbb.controller.ApiConstants;
import com.example.osbb.security.jwt.CustomAuthenticationManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Value("${jwt.secret}$")
    private String secret;

    private final String[] paths = new String[]{
            ApiConstants.RECORD, ApiConstants.RECORD + ApiConstants.SS,
            ApiConstants.SHARE, ApiConstants.SHARE + ApiConstants.SS,
            ApiConstants.USER, ApiConstants.USER + ApiConstants.SS,
            ApiConstants.ROLE, ApiConstants.ROLE + ApiConstants.SS,
            ApiConstants.OWNER, ApiConstants.OWNER + ApiConstants.SS,
            ApiConstants.PHOTO, ApiConstants.PHOTO + ApiConstants.SS,
            ApiConstants.PASSPORT, ApiConstants.PASSPORT + ApiConstants.SS,
            ApiConstants.VEHICLE, ApiConstants.VEHICLE + ApiConstants.SS,
            ApiConstants.ADDRESS, ApiConstants.ADDRESS + ApiConstants.SS,
            // questionnaire ----------------
            ApiConstants.QUESTIONNAIRE, ApiConstants.QUESTIONNAIRE + ApiConstants.SS,
            ApiConstants.QUESTIONNAIRE + ApiConstants.ALL,
            ApiConstants.QUESTIONNAIRE + ApiConstants.RESULT + ApiConstants.SS,
            // ownership --------------------
            ApiConstants.OWNERSHIP,
            ApiConstants.OWNERSHIP + ApiConstants.ID,
            ApiConstants.OWNERSHIP + ApiConstants.SS,
            ApiConstants.OWNERSHIP + ApiConstants.OWNER + ApiConstants.APARTMENT + ApiConstants.SS,
            ApiConstants.OWNERSHIP + ApiConstants.NAME + ApiConstants.SS,
            ApiConstants.OWNERSHIP + ApiConstants.ONE_OWNERSHIP_LIST_OWNER + ApiConstants.SS,
            // registry ---------------------
            ApiConstants.REGISTRY, ApiConstants.REGISTRY + ApiConstants.SS,
            ApiConstants.REGISTRY + ApiConstants.APARTMENT + ApiConstants.PARAM_4,
            ApiConstants.REGISTRY + ApiConstants.FULLNAME + ApiConstants.ALL + ApiConstants.PARAM_3,
            ApiConstants.REGISTRY + ApiConstants.FULLNAME + ApiConstants.PARAM_3,
            ApiConstants.REGISTRY + ApiConstants.SHARE + ApiConstants.FULLNAME + ApiConstants.PARAM_3,
            ApiConstants.REGISTRY + ApiConstants.SHARE + ApiConstants.TOTAL_AREA,
            // select -----------------------
            ApiConstants.SELECT, ApiConstants.SELECT + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.TITLE + ApiConstants.APARTMENT + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.TITLE + ApiConstants.FULLNAME + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.TITLE + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.TITLE + ApiConstants.ALL,
            ApiConstants.SELECT + ApiConstants.QUESTION + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.FULLNAME + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.APARTMENT + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.DATE_DISPATCH + ApiConstants.SS,
            ApiConstants.SELECT + ApiConstants.DATE_RECEIVING + ApiConstants.SS,
    };
    private final String[] publicPath = new String[]{ApiConstants.REGISTRATION, ApiConstants.LOGIN};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            CustomAuthenticationManager customAuthenticationManager) throws Exception {

//        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//        XorCsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
//        CsrfTokenRequestHandler requestHandler = delegate::handle;
//        http.csrf((csrf) -> csrf
//                .csrfTokenRepository(tokenRepository)
//                .csrfTokenRequestHandler(requestHandler));

        http.csrf(AbstractHttpConfigurer::disable);

        http.httpBasic(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(request -> {
            var config = new CorsConfiguration();
            config.setAllowCredentials(true);
            config.setAllowedHeaders(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedOriginPatterns(List.of("*"));
            return config;
        }));

        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.POST, paths).permitAll()
                .requestMatchers(HttpMethod.PUT, paths).permitAll()
                .requestMatchers(HttpMethod.GET, paths).permitAll()
                .requestMatchers(HttpMethod.DELETE, paths).permitAll()
                .anyRequest().authenticated());

//      http.formLogin((form) -> form
//                 .loginPage("/login").permitAll())
//                 .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    AuthenticationFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager,
                                                    AuthenticationConverter authenticationConverter) {
        AuthenticationFilter bearerAuthenticationFilter =
                new AuthenticationFilter(authenticationManager, authenticationConverter);
        return bearerAuthenticationFilter;
    }

}


// http.csrf(AbstractHttpConfigurer::disable);

//      XorCsrfTokenRequestAttributeHandler xorCsrfTokenRequestAttributeHandler = new XorCsrfTokenRequestAttributeHandler();
//      HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
//      CookieCsrfTokenRepository cookieCsrfTokenRepository =  CookieCsrfTokenRepository.withHttpOnlyFalse();

// ***** csrf protection cookies *****

//      http.csrf(csrf -> csrf.csrfTokenRequestHandler(xorCsrfTokenRequestAttributeHandler)
//                 .csrfTokenRepository(cookieCsrfTokenRepository)
//                 .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(cookieCsrfTokenRepository))
//                 .ignoringRequestMatchers(new AntPathRequestMatcher("/**"))
//                 .requireCsrfProtectionMatcher((RequestMatcher) new AntPathMatcher("/api/**"))
//      );

// ***** csrf protection http header *****
//      http.csrf(csrf -> csrf.csrfTokenRequestHandler(xorCsrfTokenRequestAttributeHandler)
//                .csrfTokenRepository(httpSessionCsrfTokenRepository)
//                .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(httpSessionCsrfTokenRepository))
//                .ignoringRequestMatchers("/api/**")
//                .requireCsrfProtectionMatcher((RequestMatcher) new AntPathMatcher("/api/**"))
//      );