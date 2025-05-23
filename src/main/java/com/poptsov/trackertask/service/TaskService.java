package com.poptsov.trackertask.service;

import com.poptsov.trackertask.dto.CreateTaskDto;
import com.poptsov.trackertask.dto.ReadTaskDto;
import com.poptsov.trackertask.dto.UpdateTaskDto;
import com.poptsov.trackertask.entity.Task;
import com.poptsov.trackertask.entity.User;
import com.poptsov.trackertask.exception.ResourceNotFoundException;
import com.poptsov.trackertask.mapper.TaskMapper;
import com.poptsov.trackertask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    public ReadTaskDto createTask(CreateTaskDto task, User user) {
        Task newTask = Task.builder()
                .user(user)
                .title(task.title())
                .description(task.description())
                .dueDate(task.dueDate())
                .isCompleted(false)
                .build();

        return taskMapper.taskToReadTaskDto(taskRepository.save(newTask));
    }

    public ReadTaskDto updateTask(Long taskId, UpdateTaskDto dto, User user) {
        Task task = taskRepository.findByIdAndUserId(taskId, user.getId()).orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.description() != null) {
            task.setDescription(dto.description());
        }

        if (dto.dueDate() != null) {
            task.setDueDate(dto.dueDate());
        }

        if (dto.isCompleted() != null) {
            task.setCompleted(dto.isCompleted());
        }

        return taskMapper.taskToReadTaskDto(taskRepository.save(task));
    }


    public void deleteTask(Long taskId, User user) {
        Task task = taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    public List<ReadTaskDto> getAllUserTasks(User user) {
        return taskRepository.findByUserId(user.getId())
                .stream()
                .map(taskMapper::taskToReadTaskDto)
                .toList();
    }

    public ReadTaskDto getTaskById(Long taskId, User user) {
        return taskMapper.taskToReadTaskDto(taskRepository.findByIdAndUserId(taskId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found")));
    }
}
//    @Scheduled(fixedRate = 3600000) // Каждый час
//    public void sendReminders() {
//        LocalDateTime now = LocalDateTime.now();
//        List<Task> tasks = taskRepository.findByDueDateBetweenAndReminderSentFalse(
//                now, now.plusDays(1));
//
//        tasks.forEach(task -> {
//            // Логика отправки напоминания (email/websocket)
//            task.setReminderSent(true);
//            taskRepository.save(task);
//        });
//    }