/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.config;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import microsoft.servicefabric.simplemodel.springboot.PartitionResolutionHandler;

/**
 * Configuration related to Web MVC.
 */
@EnableWebMvc
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /*
         * The todo item service is designed as a partitioned micro service where each partition 
         * could handle a portion of the employee requests. 
         * Each partition here has its own cache for lookups and talks to the backend DB for cache-miss. 
         * This is one way in which the load is effectively distributed across different partitions of 
         * the micro service. 

         * The SF PartitionResolutionHandler interceptor allows user to register partition computation logic which 
         * will be used to determine if the webrequest has landed on the partition,
         *  if not, the request is routed to the correct partition.
         * The compute function gets the id of the employee from the http request and computes modulo using the number
         *  of partitions available, and the returns the partition key.
         * This key will then be used by the interceptor to determine if the request has to be forwarded
         *  to local web server or a remote one. 
         */
        registry.addInterceptor(new PartitionResolutionHandler((HttpServletRequest request, 
                                                                Integer numOfPartitions) -> {
            // final long id = Long.parseLong(request.get);
            // final long partitionKey = id % numOfPartitions;
            try {
                final BufferedReader reader = request.getReader();
                final StringBuffer sb = new StringBuffer();
                int bytesRead;
                final char[] charBuffer = new char[128];
                while ((bytesRead = reader.read(charBuffer)) != -1) {
                    sb.append(charBuffer, 0, bytesRead);
                }
                System.out.println(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Long.toString(0);
        }));
    }

}
