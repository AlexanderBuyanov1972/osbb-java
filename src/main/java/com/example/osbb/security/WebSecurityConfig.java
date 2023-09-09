package com.example.osbb.security;

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
            "/user", "/user/**",
            "/role", "/role/**",
            "/owner", "/owner/**",
            "/photo", "/photo/**",
            "/password", "/password/**",
            "/address", "/address/**",
            "/ownership", "/ownership/**", "/ownership/name/**","/ownership/one_ownership_list_owner/**",
            "/registry/**"

    };
    private final String[] publicPath = new String[]{"/registration", "/login"};

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