package com.epam.finaltask.mapper;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.user.UserRequestDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.model.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
//    User toUser(User userDTO);
//    UserDTO toUserDTO(User user);

    UserResponseDto toUserResponseDto(User user);
    User toUser(UserRequestDto userRequestDto);
}
