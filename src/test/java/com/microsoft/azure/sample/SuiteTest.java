/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample;

import com.microsoft.azure.sample.model.TodoItemTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TodoItemTest.class, //test case 1
    TodoApplicationTest.class //test case 2
})

public class SuiteTest {
}
