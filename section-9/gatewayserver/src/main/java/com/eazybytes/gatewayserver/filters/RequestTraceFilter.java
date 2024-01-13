package com.eazybytes.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//Notes on correlation id:
//to secure our microservices communication we must make sure that they are using some sort of id
//which binds them into one group, it is nothing but correlationid
//when client initially sends the request, it will not be having that id, we've to create and attach it to the request using request headers
//that is what this class is doing, with help of Interface GlobalFilter
//this particular id will be shared across different microsercices
//like for example, if client request initially hits accounts and goes in this order, accounts->loans->cards
//then this id will be shared across all different microservices

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isCorrelationIdPresent(requestHeaders)) {
//            if present then proceed call to microservice
            logger.debug("eazyBank-correlation-id found in RequestTraceFilter : {}",
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
//            if not present then we've to create one. using filterUtility class
            String correlationID = generateCorrelationId();
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            logger.debug("eazyBank-correlation-id generated in RequestTraceFilter : {}", correlationID);
        }
        /*
         * doing below because after applying our custom filter, gateqwqy server has it's own predefined
         * filter which it has to execute, if custom filters were not implemented, then this would happen automatically
         * but when custom filters are in palce, we've to manually call predefined filters after completing our custom filter logic
         */
        return chain.filter(exchange);
    }

//    to check if the isCorrelationIdPresent
    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        if (filterUtility.getCorrelationId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

//    if the request doesn't have correlation id, then we;re creating it
    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

}