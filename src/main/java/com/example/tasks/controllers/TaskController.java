package com.example.tasks.controllers;

import com.example.tasks.controllers.dto.RequestTaskDto;
import com.example.tasks.model.Page;
import com.example.tasks.model.Task;
import com.example.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class TaskController {

    private final TaskService service;

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Task> createTask(@RequestBody @Valid RequestTaskDto dto) {
        Task newTask = RequestDtoToTaskMapper.mapToTask(dto);
        return new ResponseEntity<>(service.createNew(newTask), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Task updateTask(@RequestBody @Valid RequestTaskDto dto, @PathVariable Long id) {
        Task task = RequestDtoToTaskMapper.mapToTask(dto);
        return service.update(task, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        Optional<Task> optional = service.getById(id);
        return optional
            .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping({"", "/"})
    public List<Task> getAll(
        @RequestParam(defaultValue = "10") @Min(1) @Max(500) int pageSize,
        @RequestParam(defaultValue = "0") @Min(0) int pageNumber
    ) {
        Page page = new Page(pageSize, pageNumber);
        return service.getAll(page);
    }

    @DeleteMapping("/{id}")
    public Task deleteById(@PathVariable Long id) {
        return service.delete(id);
    }
}
