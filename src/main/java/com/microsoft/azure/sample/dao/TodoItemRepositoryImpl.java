package com.microsoft.azure.sample.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;

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
        try {
            todoItemRepository.save(todoItem);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return todoItem;
    }
    
    /**
     * Fetches Employee information by id from backend for the first time and creates an entry in the cache, which will be used from next time with out executing the method.
     */
    @Override
    @Cacheable(value="ItemCache")
    public TodoItem findOne(String index){
        return todoItemRepository.findOne(index);
    }

    /**
     * Removes the Employee information by id from backend and evicts the same from cache.
     */
    @Override
    @Cacheable(value="ItemCache")
    public List<TodoItem> findAll(){
        return todoItemRepository.findAll();
    }

    @Override
    @CacheEvict(value="ItemCache")
    public void deleteItem(String itemID){
        todoItemRepository.delete(itemID);
    }


}