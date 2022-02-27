package com.example.tasks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private ZonedDateTime modificationTime;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
