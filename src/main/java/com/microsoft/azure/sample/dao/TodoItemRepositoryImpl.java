package com.microsoft.azure.sample.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.microsoft.azure.sample.model.TodoItem;

/**
 * Implementation of DirectoryService.
 */
public class TodoItemRepositoryImpl implements TodoItemService {

    @Autowired
    TodoItemRepository todoItemRepository;
    
    /**
     * Saves Employee object in the backend database and also creates an entry in the cache.
     */
    @Override
    @CachePut(value="ItemCache")
    public TodoItem createItem(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }
    
    /**
     * Fetches Employee information by id from backend for the first time and creates an entry in the cache, which will be used from next time with out executing the method.
     */
    @Override
    @Cacheable(value="EmployeeCache", key="#id")
    public TodoItem getById(long id) {
        return todoItemRepository.findOne(id);
    }

    /**
     * Removes the Employee information by id from backend and evicts the same from cache.
     */
    @Override
    @CacheEvict(value="EmployeeCache", key="#id")
    public void removeEmployee(long id) {
        directoryRepository.delete(id);
    }

}