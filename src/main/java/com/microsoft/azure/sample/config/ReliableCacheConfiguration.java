package com.microsoft.azure.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import microsoft.servicefabric.data.reliablecollections.spring.manager.ReliableCacheManager;
import microsoft.servicefabric.data.reliablecollections.spring.provider.ReliableCacheProvider;

/**
 * Configuration to enable caching through Reliable Cache Manager.
 */
@Configuration
public class ReliableCacheConfiguration {

    /**
     * Gets the Reliable Cache Provider
     * @return returns new ReliableCacheProvider instance
     */
    public ReliableCacheProvider cachingProvider() { 
        return new ReliableCacheProvider(); 
    } 
    
    /**
     * Bean to define Reliable Cache Manager. 
     * This enables spring to use Reliable Cache Provider for caching.
     * @return returns ReliableCacheManager instance
     */
    @Bean 
    public CacheManager cacheManager() { 
        ReliableCacheManager cacheManager = cachingProvider().getCacheManager(); 
        return cacheManager; 
    } 

}