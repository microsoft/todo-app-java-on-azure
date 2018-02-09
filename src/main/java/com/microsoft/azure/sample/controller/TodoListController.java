/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.controller;

import com.microsoft.azure.sample.TodoAppLogger;
import com.microsoft.azure.sample.dao.TodoItemRepository;
import com.microsoft.azure.sample.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TodoListController {

    @Autowired
    private TodoItemRepository todoItemRepository;

    public TodoListController() {
    }

    @RequestMapping("/home")
    public Map<String, Object> home() {
        TodoAppLogger.getInstance().log("home", "get home page");
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "home");
        return model;
    }

    /**
     * HTTP GET
     */
    @RequestMapping(value = "/api/todolist/{index}",
            method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getTodoItem(@PathVariable("index") String index) {
        try {
            TodoAppLogger.getInstance().log("getTodoItem", "get todo item: " + index);
            return new ResponseEntity<TodoItem>(todoItemRepository.findOne(index), HttpStatus.OK);
        } catch (Exception e) {
            TodoAppLogger.getInstance().log("getTodoItem", "failed to get todo item " + index);
            return new ResponseEntity<String>(index + " not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP GET ALL
     */
    @RequestMapping(value = "/api/todolist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllTodoItems() {
        try {
            TodoAppLogger.getInstance().log("getAll", "get all items");
            return new ResponseEntity<List<TodoItem>>(todoItemRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            TodoAppLogger.getInstance().log("getAll", "failed to get all items");
            return new ResponseEntity<String>("Nothing found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP POST NEW ONE
     */
    @RequestMapping(value = "/api/todolist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewTodoItem(@RequestBody TodoItem item) {
        try {
            item.setID(UUID.randomUUID().toString());
            TodoAppLogger.getInstance().log("addNewTodoItem", "add a new todo item: " + item.getID());
            todoItemRepository.save(item);
            return new ResponseEntity<String>("Entity created", HttpStatus.CREATED);
        } catch (Exception e) {
            TodoAppLogger.getInstance().log("addNewTodoItem", "failed to add a new todo item");
            return new ResponseEntity<String>("Entity creation failed", HttpStatus.CONFLICT);
        }
    }

    /**
     * HTTP PUT UPDATE
     */
    @RequestMapping(value = "/api/todolist", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTodoItem(@RequestBody TodoItem item) {
        try {
            TodoAppLogger.getInstance().log("updateTodoItem", "update a todo item " + item.getID());
            todoItemRepository.delete(item.getID());
            todoItemRepository.save(item);
            return new ResponseEntity<String>("Entity updated", HttpStatus.OK);
        } catch (Exception e) {
            TodoAppLogger.getInstance().log("updateTodoItem", "failed to update a todo item " + item.getID());
            return new ResponseEntity<String>("Entity updating failed", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP DELETE
     */
    @RequestMapping(value = "/api/todolist/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTodoItem(@PathVariable("id") String id) {
        try {
            TodoAppLogger.getInstance().log("deleteTodoItem", "delete a todo item " + id);
            todoItemRepository.delete(id);
            return new ResponseEntity<String>("Entity deleted", HttpStatus.OK);
        } catch (Exception e) {
            TodoAppLogger.getInstance().log("deleteTodoItem", "failed to delete a todo item " + id);
            return new ResponseEntity<String>("Entity deletion failed", HttpStatus.NOT_FOUND);
        }

    }
}
