package com.epam.finaltask.service;

import java.util.UUID;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	@Override
	public UserDTO register(UserDTO userDTO) {
		return null;
	}

	@Override
	public UserDTO updateUser(String username, UserDTO userDTO) {
		return null;
	}

	@Override
	@Transactional
	public UserDTO getUserByUsername(String username) {
		User user = userRepository.findUserByUsername(username).orElseThrow(EntityNotFoundException::new);
		return userMapper.toUserDTO(user);
	}

	@Override
	@Transactional
	public UserDTO changeAccountStatus(UserDTO userDTO) {
		UUID userId = userMapper.toUser(userDTO).getId();
		User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
		user.setActive(userDTO.isActive());

		User saved = userRepository.save(user);
		return userMapper.toUserDTO(saved);
	}

	@Override
	@Transactional
	public UserDTO getUserById(UUID id) {
		User user =  userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
		return userMapper.toUserDTO(user);
	}

}
