package com.Task_Forge.Microservice.Service;

import com.Task_Forge.Microservice.DTO.TaskDTO;
import com.Task_Forge.Microservice.ENUM.TaskStatus;
import com.Task_Forge.Microservice.Entity.Project;
import com.Task_Forge.Microservice.Entity.Task;
import com.Task_Forge.Microservice.Entity.User;
import com.Task_Forge.Microservice.Exception.ResourceNotFoundException;
import com.Task_Forge.Microservice.Repository.ProjectRepository;
import com.Task_Forge.Microservice.Repository.TaskRepository;
import com.Task_Forge.Microservice.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Task createTask(TaskDTO taskDTO, UUID projectId, UUID assignedToId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User assignedUser = userRepository.findById(assignedToId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(TaskStatus.TO_DO);
        task.setProject(project);
        task.setAssignedTo(assignedUser);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }


    @Transactional
    public Task updateTask(UUID taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        // Check if the user trying to update the task is the assigned user
        if (!task.getAssignedTo().getEmail().equals(currentUserEmail)) {
            throw new AccessDeniedException("You do not have permission to update this task");
        }

        // Updating task fields
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(TaskStatus.valueOf(taskDTO.getStatus().toUpperCase()));

        return taskRepository.save(task);
    }
}
