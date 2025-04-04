package com.Task_Forge.Microservice.Controller;

import com.Task_Forge.Microservice.DTO.TaskDTO;
import com.Task_Forge.Microservice.Entity.Task;
import com.Task_Forge.Microservice.Entity.User;
import com.Task_Forge.Microservice.Exception.ResourceNotFoundException;
import com.Task_Forge.Microservice.Repository.UserRepository;
import com.Task_Forge.Microservice.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskDTO taskDTO){
        return ResponseEntity.ok(taskService.createTask(taskDTO, taskDTO.getProjectId(), taskDTO.getAssignedTo()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable UUID id){
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PreAuthorize("hasRole('ASSIGNED_USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @RequestBody TaskDTO taskDTO){
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @GetMapping("/count/last-7-days/{userId}")
    public ResponseEntity<Long> getTasksCount(@PathVariable UUID userId){
        long count = taskService.getTasksCountLast7Days(userId);
        return ResponseEntity.ok(count);
    }

    @PreAuthorize(("hasRole('ASSIGNED_USER') or hasRole('MANAGER') or hasRole('ADMIN')"))
    @GetMapping("/completed-last-week")
    public ResponseEntity<Map<String, Integer>> getCompletedTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserId = authentication.getName();

        User user = userRepository.findByEmail(loggedInUserId);

        if(user == null){
            throw new ResourceNotFoundException("User not found");
        }

        UUID employeeId = user.getId();
        int taskCount = taskService.getCompletedTasksCount(employeeId);
        return ResponseEntity.ok(Map.of("totalCompletedTasks", taskCount));
    }

    @GetMapping("/due-soon-tasks")
    public ResponseEntity<Long> countDueSoonTasks(){
        long count = taskService.countDueSoonTasks();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/updated-last-7-days")
    public ResponseEntity<Long> getTasksUpdatedLast7Days(){
        long count = taskService.countUpdatesInLast7Days();
        return ResponseEntity.ok(count);
    }
}
