/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import microsoft.servicefabric.reliablecache.spring.ReliableCacheManager;
import microsoft.servicefabric.data.reliablecollections.jsr107.provider.ReliableCacheProvider;

/**
 * Configuration to enable caching through Reliable Cache Manager.
 */
@Configuration
@EnableCaching
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
        final ReliableCacheManager cacheManager = new ReliableCacheManager(cachingProvider().getCacheManager());
        return cacheManager; 
    }

}
