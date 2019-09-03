/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure;

import org.junit.Test;

public class ConfigurationTest {
    @Test
    public void workspacefoldertest() {       
        System.out.println("[OUTPUT-env]: " + System.getenv("key"));
        System.out.println("[OUTPUT-vmargs]: " +(String) System.getProperties().get("foo")); 
        System.out.println("[OUTPUT-workingDirectory]: " + System.getProperty("user.dir")); 
    }

}