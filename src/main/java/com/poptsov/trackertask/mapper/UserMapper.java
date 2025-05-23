package com.poptsov.trackertask.mapper;

import com.poptsov.trackertask.dto.ReadUserDto;

import com.poptsov.trackertask.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(source = "email", target = "email")
    @Mapping(source = "createdAt", target = "createdAt")
    ReadUserDto userToReadUserDto(User user);
}