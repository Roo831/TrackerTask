package com.poptsov.trackertask.service;

import com.poptsov.trackertask.dto.ReadUserDto;
import com.poptsov.trackertask.dto.RegisterDto;
import com.poptsov.trackertask.dto.UpdateUserDto;
import com.poptsov.trackertask.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface UserService extends UserDetailsService {
    User createUser(RegisterDto request);

    ReadUserDto getByEmail(String username);

    ReadUserDto updateUser(UpdateUserDto updateUserDto, User user);
}