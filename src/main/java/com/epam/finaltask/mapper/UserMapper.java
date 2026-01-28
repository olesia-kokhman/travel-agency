package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.user.UserRequestDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequestDto userRequestDto);
    UserResponseDto toDto(User user);
    void updateFromUserDto(UserRequestDto userRequestDto, @MappingTarget User user);
}
