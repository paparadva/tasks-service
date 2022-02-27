package com.example.tasks.controllers;

import com.example.tasks.controllers.dto.RequestTaskDto;
import com.example.tasks.model.Task;

class RequestDtoToTaskMapper {

    static Task mapToTask(RequestTaskDto taskDto) {
        return new Task(taskDto.getName(), taskDto.getDescription());
    }
}
