package com.epam.finaltask.mapper;

import com.epam.finaltask.auth.dto.RegisterRequestDto;
import com.epam.finaltask.dto.user.UserAccessUpdateDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.dto.user.UserUpdateProfileDto;
import com.epam.finaltask.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterRequestDto userRegisterDto);
    UserResponseDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccessFromDto(UserAccessUpdateDto dto, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    void updateUserProfileFromDto(UserUpdateProfileDto dto, @MappingTarget User user);
}
