/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.azure.sample;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.microsoft.azure.sample.controller.TodoListController;
import com.microsoft.azure.sample.dao.TodoItemRepository;
import com.microsoft.azure.sample.model.TodoItem;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoListController.class)
public class TodoApplicationTest {
    static final String MOCK_ID = "mockId";
    static final String MOCK_DESC = "Mock Item";
    static final String MOCK_OWNER = "Owner of mock item";
    final Map<String, TodoItem> repository = new HashMap<>();
    final TodoItem mockItemA = new TodoItem(MOCK_ID + "-A", MOCK_DESC + "-A", MOCK_OWNER + "-A");
    final TodoItem mockItemB = new TodoItem(MOCK_ID + "-B", MOCK_DESC + "-B", MOCK_OWNER + "-B");

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoItemRepository todoItemRepository;

    @Before
    public void setUp() {
        repository.clear();
        repository.put(mockItemA.getID(), mockItemA);
        repository.put(mockItemB.getID(), mockItemB);

        given(this.todoItemRepository.save(any(TodoItem.class))).willAnswer((InvocationOnMock invocation) -> {
            final TodoItem item = invocation.getArgumentAt(0, TodoItem.class);
            if (repository.containsKey(item.getID())) {
                throw new Exception("Conflict.");
            }
            repository.put(item.getID(), item);
            return item;
        });

        given(this.todoItemRepository.findOne(any(String.class))).willAnswer((InvocationOnMock invocation) -> {
            final String id = invocation.getArgumentAt(0, String.class);
            return repository.get(id);
        });

        given(this.todoItemRepository.findAll()).willAnswer((InvocationOnMock invocation) -> {
            return new ArrayList<TodoItem>(repository.values());
        });

        willAnswer((InvocationOnMock invocation) -> {
            final String id = invocation.getArgumentAt(0, String.class);
            if (!repository.containsKey(id)) {
                throw new Exception("Not Found.");
            }
            repository.remove(id);
            return null;
        }).given(this.todoItemRepository).delete(any(String.class));
    }

    @After
    public void tearDown() {
        repository.clear();
    }

    @Test
    public void shouldRenderDefaultTemplate() throws Exception {
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(forwardedUrl("index.html"));
    }

    @Test
    public void canGetTodoItem() throws Exception {
        mockMvc.perform(get(String.format("/api/todolist/%s", mockItemA.getID()))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{\"id\":\"%s\",\"description\":\"%s\",\"owner\":\"%s\"}",
                        mockItemA.getID(), mockItemA.getDescription(), mockItemA.getOwner())));
    }

    @Test
    public void canGetAllTodoItems() throws Exception {
        mockMvc.perform(get("/api/todolist")).andDo(print()).andExpect(status().isOk()).andExpect(content()
                .json(String.format("[{\"id\":\"%s\"}, {\"id\":\"%s\"}]", mockItemA.getID(), mockItemB.getID())));
    }

    @Test
    public void canSaveTodoItems() throws Exception {
        final int size = repository.size();
        final TodoItem mockItemC = new TodoItem(null, MOCK_DESC + "-C", MOCK_OWNER + "-C");
        mockMvc.perform(post("/api/todolist").contentType(MediaType.APPLICATION_JSON_VALUE).content(String
                .format("{\"description\":\"%s\",\"owner\":\"%s\"}", mockItemC.getDescription(), mockItemC.getOwner())))
                .andDo(print()).andExpect(status().isCreated());
        assertTrue(size + 1 == repository.size());
    }

    @Test
    public void canDeleteTodoItems() throws Exception {
        final int size = repository.size();
        mockMvc.perform(delete(String.format("/api/todolist/%s", mockItemA.getID()))).andDo(print())
                .andExpect(status().isOk());
        assertTrue(size - 1 == repository.size());
        assertFalse(repository.containsKey(mockItemA.getID()));
    }

    @Test
    public void canUpdateTodoItems() throws Exception {
        final String newItemJsonString = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"owner\":\"%s\"}",
                mockItemA.getID(), mockItemA.getDescription(), "New Owner");
        mockMvc.perform(put("/api/todolist").contentType(MediaType.APPLICATION_JSON_VALUE).content(newItemJsonString))
                .andDo(print()).andExpect(status().isOk());
        assertTrue(repository.get(mockItemA.getID()).getOwner().equals("New Owner"));
    }

    @Test
    public void canNotDeleteNonExistingTodoItems() throws Exception {
        final int size = repository.size();
        mockMvc.perform(delete(String.format("/api/todolist/%s", "Non-Existing-ID"))).andDo(print())
                .andExpect(status().isNotFound());
        assertTrue(size == repository.size());
    }

    /**
     * PUT should be idempotent.
     */
    @Test
    public void idempotenceOfPut() throws Exception {
        final String newItemJsonString = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"owner\":\"%s\"}",
                mockItemA.getID(), mockItemA.getDescription(), "New Owner");
        mockMvc.perform(put("/api/todolist").contentType(MediaType.APPLICATION_JSON_VALUE).content(newItemJsonString))
                .andDo(print()).andExpect(status().isOk());
        final TodoItem firstRes = repository.get(mockItemA.getID());
        mockMvc.perform(put("/api/todolist").contentType(MediaType.APPLICATION_JSON_VALUE).content(newItemJsonString))
                .andDo(print()).andExpect(status().isOk());
        final TodoItem secondRes = repository.get(mockItemA.getID());
        assertTrue(firstRes.equals(secondRes));
    }
}
