package com.example.tasks.service;

import com.example.tasks.model.Page;
import com.example.tasks.model.Task;
import com.example.tasks.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository mockRepository;

    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(mockRepository);
    }

    @Test
    void shouldReturnSortedSecondPage() {
        // Arrange
        ArrayList<Task> listOfTasks = createListOfTasks(15);

        when(mockRepository.getAll()).thenReturn(listOfTasks);

        Page page = new Page(5, 1);

        // Act
        List<Task> result = taskService.getAll(page);

        // Assert
        assertEquals(result.size(), 5);
        assertEquals(result.get(0).getId(), 9L);
        assertEquals(result.get(1).getId(), 8L);
        assertEquals(result.get(2).getId(), 7L);
        assertEquals(result.get(3).getId(), 6L);
        assertEquals(result.get(4).getId(), 5L);
    }

    private ArrayList<Task> createListOfTasks(int size) {
        ZonedDateTime start = ZonedDateTime.parse("2022-02-25T07:00:00Z");

        var listOfTasks = new ArrayList<Task>();
        for (int i = 0; i < size; i++) {
            listOfTasks.add(new Task((long) i, "", "", start.plusSeconds(i)));
        }
        return listOfTasks;
    }
}