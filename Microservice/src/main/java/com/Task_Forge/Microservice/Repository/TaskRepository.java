package com.Task_Forge.Microservice.Repository;

import com.Task_Forge.Microservice.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
