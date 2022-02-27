package com.example.tasks.controllers.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RequestTaskDto {

    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Description must not be null")
    private String description;

    public RequestTaskDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
