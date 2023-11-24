package com.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

//	custom routing using spring cloud gateway
	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder){
		return routeLocatorBuilder.routes()
				.route(p -> p
//						defining custom path
//						instead of following default path provided by gatewayserver
//						it makes more sense to add organisation(eazybank) name in url, so adding it
//						so whenever client calls to gateway server they must send the url in below format only
						.path("/eazybank/accounts/**")
//						rewriting the path
//						(?<segment>.*)  -> remaining path url
//						"/eazybank/accounts/(?<segment>.*)" -> when we send this path to the accounts microservice, it will fail
//						because in accounts microservice this path doesn't exist
//						so before call actually goes to accounts microservice, we are rewriting the path by removing  /eazybank/accounts
//						and just sending the leftover url as /${segment}
						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}"))
//						doing load balancing saying that do load balancing(lb) in ACCOUNTS microservice instances (in eurekaserver)
						.uri("lb://ACCOUNTS"))
				.route(p -> p
						.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}"))
						.uri("lb://LOANS"))
				.route(p -> p
						.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}"))
						.uri("lb://CARDS")).build();
	}

}
