/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TodoItemTest {

    @Test
    public void testEqualsObject() {
        final TodoItem itemA = new TodoItem();
        final TodoItem itemB1 = new TodoItem("B", "Item B", "Owner of Item B");
        final TodoItem itemB2 = new TodoItem("B", "Item B", "Owner of Item B");
        final Object nonTodoItem = new Object();
        assertTrue(itemA.equals(itemA));
        assertFalse(itemA.equals(null));
        assertFalse(itemA.equals(nonTodoItem));
        assertFalse(itemA.equals(itemB1));
        assertTrue(itemB1.equals(itemB2));
        assertFalse(itemB1.equals(itemA));
    }

}
