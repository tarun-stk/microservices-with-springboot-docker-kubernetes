package com.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

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
//						this path takes a predicate, if path matches returns true, and goes forwared
//						if return false, then won't allow going forward
						.path("/eazybank/accounts/**")
//						rewriting the path
//						(?<segment>.*)  -> remaining path url
//						"/eazybank/accounts/(?<segment>.*)" -> when we send this path to the accounts microservice, it will fail
//						because in accounts microservice this path doesn't exist
//						so before call actually goes to accounts microservice, we are rewriting the path by removing  /eazybank/accounts
//						and just sending the leftover url as /${segment}

						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
//						in this filters you can also add responseheaders that you want to send to your client
//						like X-Response-Time --> this indicates when did you send the response, so it allows to
//						estimate the actual time taken by the server side app to process the request and send a response
//						by subtracting request time and response time
//						addResponseHeader() is a predefined filter available
//						we can also write our own filter methods
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
//						doing load balancing saying that do load balancing(lb) in ACCOUNTS microservice instances (in eurekaserver)
//						i.e send request to appropriate microservice
						.uri("lb://ACCOUNTS"))
				.route(p -> p
						.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://LOANS"))
				.route(p -> p
						.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();
	}

}
