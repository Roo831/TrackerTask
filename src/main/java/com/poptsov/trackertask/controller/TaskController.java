package com.poptsov.trackertask.controller;


import com.poptsov.trackertask.dto.CreateTaskDto;
import com.poptsov.trackertask.dto.ReadTaskDto;
import com.poptsov.trackertask.dto.UpdateTaskDto;
import com.poptsov.trackertask.entity.User;
import com.poptsov.trackertask.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<ReadTaskDto>> getAllTasks(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.getAllUserTasks(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadTaskDto> getTaskById(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.getTaskById(id, user));
    }

    @PostMapping
    public ResponseEntity<ReadTaskDto> createTask(
            @Valid @RequestBody CreateTaskDto task,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.createTask(task, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadTaskDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDto task,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(taskService.updateTask(id, task, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }
}