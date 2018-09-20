/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.microsoft.azure.sample.model.TodoItem;

/**
 * Implementation of todoItemService.
 */
@Service
public class TodoItemServiceImpl implements TodoItemService {

    @Autowired
    TodoItemRepository todoItemRepository;
    
    /**
     * Saves Employee object in the backend database and also creates an entry in the cache.
     */
    @Override
    @CachePut(value = "ItemCache", key = "#todoItem.id")
    public TodoItem createItem(TodoItem todoItem) {
        try {
            System.out.println("Saving to repository:" + todoItem.getOwner());
            todoItemRepository.save(todoItem);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return todoItem;
    }
    
    /**
     * Fetches Employee information by id from backend for 
     * the first time and creates an entry in the cache,
     * which will be used from next time with out executing the method.
     */
    @Override
    @Cacheable(value = "ItemCache", key = "#index")
    public TodoItem findOne(String index){
        System.out.println("Finding for repository:" + index);
        return todoItemRepository.findOne(index);
    }

    /**
     * Removes the Employee information by id from backend and evicts the same from cache.
     */
    @Override
    public List<TodoItem> findAll(){
        return todoItemRepository.findAll();
    }

    @Override
    @CacheEvict(value = "ItemCache", key = "#itemID")
    public void deleteItem(String itemID){
        System.out.println("Deleting form repository:" + itemID);
        todoItemRepository.delete(itemID);
    }


}
