package com.epam.finaltask.service;

import com.epam.finaltask.auth.dto.RegisterRequestDto;
import com.epam.finaltask.dto.user.*;
import com.epam.finaltask.exception.exceptions.EmailAlreadyExistsException;
import com.epam.finaltask.exception.exceptions.UserNotFoundException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.entity.Order;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.model.enums.UserRole;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.repository.specifications.UserSpecification;
import com.epam.finaltask.repository.specifications.filters.UserFilter;
import com.epam.finaltask.util.PageableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAll(UserFilter filter, Pageable pageable) {

        Pageable effectivePageable = PageableUtils.withDefaultSort(pageable);

        Page<User> page = userRepository.findAll(UserSpecification.build(filter), effectivePageable);
        return page.map(userMapper::toDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Transactional(readOnly = true)
    public UserResponseDto getById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDto(user);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional(readOnly = true)
    public UserResponseDto getMe(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserResponseDto register(RegisterRequestDto userRegisterDto) {

        if(userRepository.existsByEmail(userRegisterDto.getEmail())) {
            throw new EmailAlreadyExistsException(userRegisterDto.getEmail());
        }

        boolean active = true;
        BigDecimal balance = BigDecimal.ZERO;
        UserRole role = UserRole.USER;
        List<Order> orders = new ArrayList<>();

        User user = new User(
                userRegisterDto.getName(),
                userRegisterDto.getSurname(),
                userRegisterDto.getEmail(),
                userRegisterDto.getPhoneNumber(),
                passwordEncoder.encode(userRegisterDto.getPassword()),
                active,
                balance,
                role,
                orders);

        User saved = userRepository.save(user);

        log.info("BUSINESS userRegistered userId={} email={} role={} active={}",
                saved.getId(), saved.getEmail(), saved.getRole(), saved.isActive());

        return userMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        if(userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new EmailAlreadyExistsException(userCreateDto.getEmail());
        }

        User user = new User(
                userCreateDto.getName(),
                userCreateDto.getSurname(),
                userCreateDto.getEmail(),
                userCreateDto.getPhoneNumber(),
                passwordEncoder.encode(userCreateDto.getPassword()),
                userCreateDto.isActive(),
                userCreateDto.getBalance(),
                userCreateDto.getRole(),
                new ArrayList<>());

        User saved = userRepository.save(user);

        log.info("BUSINESS userCreatedByAdmin userId={} email={} role={} active={}",
                saved.getId(), saved.getEmail(), saved.getRole(), saved.isActive());

        return userMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserResponseDto updateUser(UUID userId, UserAccessUpdateDto userAccessUpdateDto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        boolean oldActive = currentUser.isActive();
        UserRole oldRole = currentUser.getRole();
        BigDecimal oldBalance = currentUser.getBalance();

        userMapper.updateAccessFromDto(userAccessUpdateDto, currentUser);
        User saved = userRepository.save(currentUser);

        log.info("BUSINESS userAccessUpdated userId={} active:{}->{} role:{}->{} balance:{}->{}",
                userId,
                oldActive, saved.isActive(),
                oldRole, saved.getRole(),
                oldBalance, saved.getBalance()
        );

        return userMapper.toDto(saved);
    }

    @PreAuthorize("#userId == authentication.principal.id")
    @Transactional
    public UserResponseDto updateProfile(UUID userId, UserUpdateProfileDto userUpdateProfileDto) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        String oldEmail = currentUser.getEmail();
        String oldPhone = currentUser.getPhoneNumber();
        boolean passwordChanged = (userUpdateProfileDto.getPassword() != null && !userUpdateProfileDto.getPassword().isBlank());

        String newEmail = userUpdateProfileDto.getEmail();
        if (newEmail != null && !newEmail.equalsIgnoreCase(currentUser.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new EmailAlreadyExistsException(newEmail);
            }
            currentUser.setEmail(newEmail);
        }

        userMapper.updateUserProfileFromDto(userUpdateProfileDto, currentUser);

        if (userUpdateProfileDto.getPassword() != null && !userUpdateProfileDto.getPassword().isBlank()) {
            currentUser.setPassword(passwordEncoder.encode(userUpdateProfileDto.getPassword()));
        }

        User saved = userRepository.save(currentUser);

        log.info("BUSINESS userProfileUpdated userId={} email:{}->{} phone:{}->{} passwordChanged={}",
                userId,
                oldEmail, saved.getEmail(),
                oldPhone, saved.getPhoneNumber(),
                passwordChanged
        );


        return userMapper.toDto(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(UUID userId) {

        if(userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            log.info("BUSINESS userDeleted userId={}", userId);
        } else {
            throw new UserNotFoundException(userId);
        }
    }


}
