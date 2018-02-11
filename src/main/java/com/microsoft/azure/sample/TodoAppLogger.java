/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;

public class TodoAppLogger {
    private static TodoAppLogger todoAppLogger = null;

    private String host = "your-aws-elasticsearch-endpoint";

    private Logger logger = Logger.getLogger(TodoApplication.class.getName());

    private long id = 0;

    public void log(String type, String document) {
        final RestClient client = RestClient.builder(new HttpHost(host, 443, "https")).build();
        document = "{ \"message\": \"" + document + "\", \"time\": \"" + now() + "\", \"type\": \"" + type + "\"}";
        final HttpEntity entity = new NStringEntity(document, org.apache.http.entity.ContentType.APPLICATION_JSON);
        final Response response;
        try {
            response = client.performRequest(
                    "PUT",
                    "/azure/todoapp/" + (id++),
                    Collections.emptyMap(),
                    entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(document);
    }

    private String now() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    private TodoAppLogger() {}

    public static TodoAppLogger getInstance() {
        return todoAppLogger = todoAppLogger == null ? new TodoAppLogger() : todoAppLogger;
    }
}
