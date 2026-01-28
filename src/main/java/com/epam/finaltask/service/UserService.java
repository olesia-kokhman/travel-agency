package com.epam.finaltask.service;

import com.epam.finaltask.dto.user.UserRequestDto;
import com.epam.finaltask.dto.user.UserResponseDto;
import com.epam.finaltask.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    public Optional<User> findByEmail(String email) {
        return null;
    }

    public UserResponseDto create(UserRequestDto userRequestDto) {

        // encode password
        return null;
    }

}
