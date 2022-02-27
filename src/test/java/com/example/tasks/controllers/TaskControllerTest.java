package com.example.tasks.controllers;

import com.example.tasks.controllers.dto.RequestTaskDto;
import com.example.tasks.model.Task;
import com.example.tasks.model.TaskNotFoundException;
import com.example.tasks.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    public static final String TASK_NAME = "name";
    public static final String TASK_DESCRIPTION = "description";
    public static final String MODIFICATION_TIME_STRING = "2022-02-25T07:00:00Z";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService mockService;

    @Test
    void shouldGetTaskById() throws Exception {
        // Arrange
        Task taskFromService = new Task(1L, TASK_NAME, TASK_DESCRIPTION, ZonedDateTime.parse(MODIFICATION_TIME_STRING));
        when(mockService.getById(1L)).thenReturn(Optional.of(taskFromService));

        // Act
        var result = mockMvc.perform(get("/tasks/1"));

        // Assert
        result
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is(TASK_NAME)))
            .andExpect(jsonPath("$.description", is(TASK_DESCRIPTION)))
            .andExpect(jsonPath("$.modificationTime", is(MODIFICATION_TIME_STRING)));

        verify(mockService, times(1)).getById(1L);
    }

    @Test
    void shouldReturn404WhenTaskNotExists() throws Exception {
        // Arrange
        when(mockService.getById(1L)).thenReturn(Optional.empty());

        // Act
        mockMvc.perform(get("/tasks/1")).andExpect(status().isNotFound());

        // Assert
        verify(mockService, times(1)).getById(1L);
    }

    @Test
    void shouldGetListOfTasks() throws Exception {
        // Arrange
        var listOfTasks = List.of(
            new Task(1L, TASK_NAME, TASK_DESCRIPTION, ZonedDateTime.parse(MODIFICATION_TIME_STRING)),
            new Task(2L, TASK_NAME, TASK_DESCRIPTION, ZonedDateTime.parse(MODIFICATION_TIME_STRING))
        );
        when(mockService.getAll(any())).thenReturn(listOfTasks);

        // Act
        var result = mockMvc.perform(get("/tasks"));

        // Assert
        result
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[1].id", is(2)));

        verify(mockService, times(1)).getAll(any());
    }

    @Test
    void shouldCreateNewTaskWithPost() throws Exception {
        // Arrange
        Task savedTask = new Task(1L, TASK_NAME, TASK_DESCRIPTION, ZonedDateTime.parse(MODIFICATION_TIME_STRING));
        when(mockService.createNew(any(Task.class))).thenReturn(savedTask);

        RequestTaskDto inputTask = new RequestTaskDto(TASK_NAME, TASK_DESCRIPTION);

        // Act
        var result = mockMvc.perform(post("/tasks")
            .content(mapper.writeValueAsString(inputTask))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON));

        // Assert
        result.andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is(TASK_NAME)))
            .andExpect(jsonPath("$.description", is(TASK_DESCRIPTION)))
            .andExpect(jsonPath("$.modificationTime", is(MODIFICATION_TIME_STRING)));

        verify(mockService, times(1)).createNew(any(Task.class));
    }

    @Test
    void shouldReturn400IfPostingNullFields() throws Exception {
        // Arrange
        RequestTaskDto inputTask = new RequestTaskDto(null, null);

        // Act
        var result = mockMvc.perform(post("/tasks")
            .content(mapper.writeValueAsString(inputTask))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON));

        // Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateTaskWithPut() throws Exception {
        // Arrange
        Task updatedTask = new Task(1L, TASK_NAME, TASK_DESCRIPTION, ZonedDateTime.parse(MODIFICATION_TIME_STRING));
        when(mockService.update(any(Task.class), eq(1L))).thenReturn(updatedTask);

        RequestTaskDto inputTask = new RequestTaskDto(TASK_NAME, TASK_DESCRIPTION);

        // Act
        var result = mockMvc.perform(put("/tasks/1")
            .content(mapper.writeValueAsString(inputTask))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is(TASK_NAME)))
            .andExpect(jsonPath("$.description", is(TASK_DESCRIPTION)))
            .andExpect(jsonPath("$.modificationTime", is(MODIFICATION_TIME_STRING)));

        verify(mockService, times(1)).update(any(Task.class), anyLong());
    }

    @Test
    void shouldReturn404WhenPuttingToWrongId() throws Exception {
        // Arrange
        when(mockService.update(any(Task.class), eq(1L))).thenThrow(new TaskNotFoundException(1L));

        RequestTaskDto inputTask = new RequestTaskDto(TASK_NAME, TASK_DESCRIPTION);

        // Act
        var result = mockMvc.perform(put("/tasks/1")
            .content(mapper.writeValueAsString(inputTask))
            .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound());

        verify(mockService, times(1)).update(any(Task.class), anyLong());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        // Arrange
        Task deletedTask = new Task(1L, TASK_NAME, TASK_DESCRIPTION, ZonedDateTime.parse(MODIFICATION_TIME_STRING));
        when(mockService.delete(eq(1L))).thenReturn(deletedTask);

        // Act
        var result = mockMvc.perform(delete("/tasks/1"));

        // Assert
        result.andExpect(status().isOk());

        verify(mockService, times(1)).delete(anyLong());
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        // Arrange
        when(mockService.delete(eq(1L))).thenThrow(new TaskNotFoundException(1L));

        // Act
        var result = mockMvc.perform(delete("/tasks/1"));

        // Assert
        result.andExpect(status().isNotFound());

        verify(mockService, times(1)).delete(anyLong());
    }

    @Test
    void shouldReturn400WhenPageParamInInvalid() throws Exception {
        // Act
        var result = mockMvc.perform(get("/tasks/?pageSize=-1&pageNumber=-1"));

        // Assert
        result.andExpect(status().isBadRequest());
    }
}