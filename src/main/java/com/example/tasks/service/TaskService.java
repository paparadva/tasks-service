package com.example.tasks.service;

import com.example.tasks.model.Page;
import com.example.tasks.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task createNew(Task task);

    Task update(Task newData, Long id);

    List<Task> getAll(Page page);

    Optional<Task> getById(Long id);

    Task delete(Long id);
}
