package com.eazybytes.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
//if it was a normal spring boot app we use @EnableWebSecurity
//as this is spring cloud we use @EnableWebFluxSecurity
//as spring cloud we're using reactive flux
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        setting authentication mechanism
//        for all get (read) operations we're saying don't need to authenticate i.e., permitAll()
        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
//  all requests coming under eazybank/<microservicename> should be authenticated
//                however for get requests authentication shouldn't happen as it has higher priority over other requests
//                like u see mentioned at top
//                when only authenticated() is kept then role based authorization will not happen
//                        .pathMatchers("/eazybank/accounts/**").authenticated()
//                making sure authorization happens on role
                        .pathMatchers("/eazybank/accounts/**").hasRole("ACCOUNTS")
//                        .pathMatchers("/eazybank/cards/**").authenticated()
                        .pathMatchers("/eazybank/cards/**").hasRole("CARDS")
//                        .pathMatchers("/eazybank/loans/**").authenticated())
                        .pathMatchers("/eazybank/loans/**").hasRole("LOANS"))
//                converting gatewayserver to oauth2 resource server
                        .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
//                                setting default way of authenticating, means with no roles
//                            .jwt(Customizer.withDefaults()));
//                                telling spring security that set authentication based on roles which wil be extracted
//                                using grantedAuthoritiesExtractor() method
                                .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())));
//        disabling csrf as we're not sending any requests over browser
        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return serverHttpSecurity.build();
    }

//    extracts roles from Jwt access token
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter
//                calling bean where our logic is there, where we are converting Jwt to extract roles
                (new KeycloakRoleConverter());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
