package com.microsoft.azure.sample.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler; 
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import microsoft.servicefabric.simplemodel.springboot.PartitionResolutionHandler;
import microsoft.servicefabric.services.client.ServicePartitionKey;

/**
 * Configuration related to Web MVC.
 */
/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/", "classpath:/static/");
	}
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "index.html");
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
         * The employee directory service is designed as a partitioned micro service where each partition could handle a portion of the employee requests. Each partition here has its own cache for lookups and talks to the backend DB for cache-miss. This is one way in which the load is effectively distributed across different partitions of the micro service. 

         * The SF PartitionResolutionHandler interceptor allows user to register partition computation logic which will be used to determine if the webrequest has landed on the partition, if not, the request is routed to the correct partition.
         * The compute function gets the id of the employee from the http request and computes modulo using the number of partitions available, and the returns the partition key.
         * This key will then be used by the interceptor to determine if the request has to be forwarded to local web server or a remote one. 
         */
      registry.addInterceptor(
    			new PartitionResolutionHandler(
    					(HttpServletRequest request, Integer numOfPartitions) -> {
    						String query = request.getQueryString();
    						if (query == null || "".equals(query)) {
    							return "0";
    						}
    						String []tokens = query.split("=");
    						if (tokens.length != 2) {
    							return "0";
    						}
    						String id = tokens[1];
    						return Long.toString(Math.abs(id.hashCode()) % numOfPartitions);
    					}));
   }

}
