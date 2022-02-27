package com.example.tasks.repository;

import com.example.tasks.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);

    Optional<Task> getById(Long id);

    Collection<Task> getAll();

    Optional<Task> deleteById(Long id);
}
