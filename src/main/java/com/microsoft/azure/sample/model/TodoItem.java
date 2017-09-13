/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.model;

import com.microsoft.azure.spring.data.documentdb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Document(collection = "mycollection")
public class TodoItem {
    @Id
    private String id;
    private String description;
    private String owner;
    private boolean finish;

    public TodoItem() {
    }

    public TodoItem(String id, String description, String owner) {
        this.description = description;
        this.id = id;
        this.owner = owner;
        this.finish = false;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || !(o instanceof TodoItem)) {
            return false;
        }
        final TodoItem group = (TodoItem) o;
        return this.getDescription().equals(group.getDescription())
                && this.getOwner().equals(group.getOwner())
                && this.getID().equals(group.getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, id, owner);
    }
}

