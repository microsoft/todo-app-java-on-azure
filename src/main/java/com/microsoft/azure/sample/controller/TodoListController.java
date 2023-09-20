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
        final Map<String, Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "home");
        return model;
    }

    /**
     * HTTP GET
     */
    @GetMapping(value = "/api/todolist/{index}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getTodoItem(@PathVariable("index") String index) {
        try {
            return new ResponseEntity<>(todoItemRepository.findById(index).get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(index + " not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP GET ALL
     */
    @GetMapping(value = "/api/todolist", produces = {MediaType.APPLICATION_JSON_VALUE})
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
    @PostMapping(value = "/api/todolist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addNewTodoItem(@RequestBody TodoItem item) {
        try {
            item.setID(UUID.randomUUID().toString());
            todoItemRepository.save(item);
            return new ResponseEntity<>("Entity created", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Entity creation failed", HttpStatus.CONFLICT);
        }
    }

    /**
     * HTTP PUT UPDATE
     */
    @PutMapping(value = "/api/todolist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTodoItem(@RequestBody TodoItem item) {
        try {
            todoItemRepository.deleteById(item.getID());
            todoItemRepository.save(item);
            return new ResponseEntity<>("Entity updated", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Entity updating failed", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * HTTP DELETE
     */
    @DeleteMapping(value = "/api/todolist/{id}")
    public ResponseEntity<String> deleteTodoItem(@PathVariable("id") String id) {
        try {
            todoItemRepository.deleteById(id);
            return new ResponseEntity<>("Entity deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Entity deletion failed", HttpStatus.NOT_FOUND);
        }

    }
}
