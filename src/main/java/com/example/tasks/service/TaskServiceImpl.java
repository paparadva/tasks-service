package com.example.tasks.service;

import com.example.tasks.model.Page;
import com.example.tasks.model.Task;
import com.example.tasks.model.TaskNotFoundException;
import com.example.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    @Override
    public Task createNew(Task task) {
        synchronized (repository) {
            task.setId(null);
            task.setModificationTime(ZonedDateTime.now(ZoneOffset.UTC));
            return repository.save(task);
        }
    }

    @Override
    public Task update(Task newData, Long id) {
        synchronized (repository) {
            Task existing = repository.getById(id).orElseThrow(() -> new TaskNotFoundException(id));

            existing.setName(Objects.requireNonNullElse(newData.getName(), existing.getName()));
            existing.setDescription(Objects.requireNonNullElse(newData.getDescription(), existing.getDescription()));
            existing.setModificationTime(ZonedDateTime.now(ZoneOffset.UTC));

            return repository.save(existing);
        }
    }

    @Override
    public List<Task> getAll(Page page) {
        synchronized (repository) {
            return repository.getAll().stream()
                .sorted(Comparator.comparing(Task::getModificationTime).reversed())
                .skip((long) page.getPageSize() * page.getPageNumber())
                .limit(page.getPageSize())
                .collect(Collectors.toList());
        }
    }

    @Override
    public Optional<Task> getById(Long id) {
        synchronized (repository) {
            return repository.getById(id);
        }
    }

    @Override
    public Task delete(Long id) {
        synchronized (repository) {
            return repository.deleteById(id).orElseThrow(() -> new TaskNotFoundException(id));
        }
    }
}
