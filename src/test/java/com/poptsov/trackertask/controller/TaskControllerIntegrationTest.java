package com.poptsov.trackertask.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.poptsov.trackertask.AbstractPostgresContainer;
import com.poptsov.trackertask.dto.CreateTaskDto;
import com.poptsov.trackertask.dto.UpdateTaskDto;
import com.poptsov.trackertask.entity.Task;
import com.poptsov.trackertask.entity.User;
import com.poptsov.trackertask.repository.TaskRepository;
import com.poptsov.trackertask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerIntegrationTest extends AbstractPostgresContainer {

    @Autowired private MockMvc mockMvc;
    @Autowired private TaskRepository taskRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    private String jwtToken;
    private User testUser;

    @BeforeEach
    void setup() throws Exception {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .email("user@example.com")
                .password(passwordEncoder.encode("password"))
                .build();
        userRepository.save(testUser);

        String json = """
                {
                  "email": "user@example.com",
                  "password": "password"
                }
                """;

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void testCreateTask() throws Exception {
        CreateTaskDto dto = new CreateTaskDto("Title", "Desc", LocalDateTime.now().plusDays(1));

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    @Test
    void testGetTasks() throws Exception {
        Task task = Task.builder()
                .title("Sample")
                .description("For Get")
                .dueDate(LocalDateTime.now().plusDays(1))
                .user(testUser)
                .build();
        taskRepository.save(task);

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Sample"));
    }

    @Test
    void testUpdateTask() throws Exception {
        Task task = Task.builder()
                .title("Old")
                .description("Old Desc")
                .dueDate(LocalDateTime.now().plusDays(1))
                .user(testUser)
                .build();
        task = taskRepository.save(task);

        UpdateTaskDto dto = new UpdateTaskDto("New", "New Desc", LocalDateTime.now().plusDays(2), true);

        mockMvc.perform(put("/api/tasks/" + task.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testDeleteTask() throws Exception {
        Task task = Task.builder()
                .title("To Delete")
                .description("...")
                .dueDate(LocalDateTime.now().plusDays(1))
                .user(testUser)
                .build();
        task = taskRepository.save(task);

        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }
}
