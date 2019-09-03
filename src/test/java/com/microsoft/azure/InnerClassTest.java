/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure;

import org.junit.Test;

public class InnerClassTest {
    @Test
    public void outmethod1() {       
        System.out.println("[OUTPUT]: This is out method");        
    }

    public static class InnerTest{
         @Test
         public void innermethod1(){            
            System.out.println("[OUTPUT]: This is inner method"); 
         }
     }
}
