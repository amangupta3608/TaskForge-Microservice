package com.Task_Forge.Microservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskDTO {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotBlank(message = "Task status is required")
    private String status;

    private UUID projectId;
    private UUID assignedTo;
}
