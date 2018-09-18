/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.dao;

import com.microsoft.azure.sample.model.*;
import java.util.List;

/**
 * Directory service to perform CURD operations in the employee directory.
 */
public interface TodoItemService {

    TodoItem findOne(String index);

    List<TodoItem> findAll();

    TodoItem createItem(TodoItem item);
    
    void deleteItem(String itemID);

}
