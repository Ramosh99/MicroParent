package org.example.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //below is how to disable security
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }*/

    //below is how to enable security
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.csrf (httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests ((requests)->requests
                        .requestMatchers ( "/eureka/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer (oauth->oauth.jwt (Customizer.withDefaults()))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder (OAuth2ResourceServerProperties properties){
        return JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri());
    }

    /*@Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.GET, "/api/auth/**").hasRole("admin")
                        .anyRequest()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(null)
                        )
                )
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }*/


}
