package com.poptsov.trackertask.controller;


import com.poptsov.trackertask.dto.ReadUserDto;
import com.poptsov.trackertask.dto.UpdateUserDto;
import com.poptsov.trackertask.entity.User;
import com.poptsov.trackertask.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ReadUserDto> getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ReadUserDto userFromDB = userService.getByEmail(user.getUsername());
        return userFromDB != null ? ResponseEntity.ok(userFromDB) : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<ReadUserDto> updateUser(Authentication authentication, @RequestBody @Valid UpdateUserDto updateUserDto) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateUser(updateUserDto, user));
    }
}