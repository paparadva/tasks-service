package com.example.tasks.repository;

import com.example.tasks.model.Task;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private long nextId = 1L;

    private final Map<Long, Task> database = new HashMap<>();

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(nextId++);
        }

        database.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> getById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Collection<Task> getAll() {
        return database.values();
    }

    @Override
    public Optional<Task> deleteById(Long id) {
        return Optional.of(database.remove(id));
    }
}
