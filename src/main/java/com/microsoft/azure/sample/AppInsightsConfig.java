/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample;

import javax.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.microsoft.applicationinsights.TelemetryConfiguration;
import com.microsoft.applicationinsights.web.internal.WebRequestTrackingFilter;;


@Configuration
public class AppInsightsConfig {

    @Bean
    public FilterRegistrationBean aiFilterRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebRequestTrackingFilter());
        registration.addUrlPatterns("/**");
        registration.setOrder(1);
        return registration;
    } 

    @Bean(name = "WebRequestTrackingFilter")
    public Filter WebRequestTrackingFilter() {
        return new WebRequestTrackingFilter();
    }
}
