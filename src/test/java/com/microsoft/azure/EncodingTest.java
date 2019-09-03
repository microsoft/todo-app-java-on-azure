/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure;

import static org.junit.Assert.assertTrue;
import java.nio.charset.Charset;
import org.junit.*;


public class EncodingTest {
   @Test
   public void test1(){
       assertTrue(true);
       System.out.println(Charset.defaultCharset());
       System.out.println("你好！");
   }
}
