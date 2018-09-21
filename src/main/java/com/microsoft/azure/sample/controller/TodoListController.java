/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample.controller;

import com.microsoft.azure.sample.dao.TodoItemRepository;
import com.microsoft.azure.sample.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class TodoListController {

    @Autowired
    private TodoItemRepository todoItemRepository;

    public TodoListController() {
    }

    @RequestMapping("/home")
    public Map<String, Object> home() {
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
            return new ResponseEntity<TodoItem>(todoItemRepository.findById(index).get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(index + " not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP GET ALL
     */
    @RequestMapping(value = "/api/todolist", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllTodoItems() {
        try {
            return new ResponseEntity<>(todoItemRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Nothing found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP POST NEW ONE
     */
    @RequestMapping(value = "/api/todolist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewTodoItem(@RequestBody TodoItem item) {
        try {
            item.setID(UUID.randomUUID().toString());
            todoItemRepository.save(item);
            return new ResponseEntity<String>("Entity created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Entity creation failed", HttpStatus.CONFLICT);
        }
    }

    /**
     * HTTP PUT UPDATE
     */
    @RequestMapping(value = "/api/todolist", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTodoItem(@RequestBody TodoItem item) {
        try {
            todoItemRepository.deleteById(item.getID());
            todoItemRepository.save(item);
            return new ResponseEntity<String>("Entity updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Entity updating failed", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP DELETE
     */
    @RequestMapping(value = "/api/todolist/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteTodoItem(@PathVariable("id") String id) {
        try {
            todoItemRepository.deleteById(id);
            return new ResponseEntity<String>("Entity deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Entity deletion failed", HttpStatus.NOT_FOUND);
        }

    }
}
