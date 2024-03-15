/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.model;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TodoItem {
    private String id;
    private String description;
    private String owner;
    private boolean finished;

    public TodoItem(String s, String s1, String s2) {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return finished == todoItem.finished &&
            Objects.equals(id, todoItem.id) &&
            Objects.equals(description, todoItem.description) &&
            Objects.equals(owner, todoItem.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, owner, finished);
    }
}

