package com.poptsov.trackertask.mapper;

import com.poptsov.trackertask.dto.CreateTaskDto;
import com.poptsov.trackertask.dto.ReadTaskDto;
import com.poptsov.trackertask.entity.Task;
import com.poptsov.trackertask.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

   @Mapping(source = "user.id", target = "userId")
   @Mapping(source = "createdAt", target = "createAt")
   ReadTaskDto taskToReadTaskDto(Task task);

}
