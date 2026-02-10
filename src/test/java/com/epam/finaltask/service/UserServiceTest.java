package com.epam.finaltask.service;

import com.epam.finaltask.auth.dto.RegisterRequestDto;
import com.epam.finaltask.dto.user.*;
import com.epam.finaltask.exception.exceptions.EmailAlreadyExistsException;
import com.epam.finaltask.exception.exceptions.UserNotFoundException;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.entity.User;
import com.epam.finaltask.model.enums.UserRole;
import com.epam.finaltask.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void existsByEmail_delegatesToRepository() {
        String email = "a@b.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.existsByEmail(email);

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void getByEmail_returnsOptionalFromRepository() {
        String email = "a@b.com";
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getByEmail(email);

        assertTrue(result.isPresent());
        assertSame(user, result.get());
        verify(userRepository, times(1)).findByEmail(email);
    }


    @Test
    void getById_whenNotFound_throwsUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getById_whenFound_returnsMappedDto() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto dto = new UserResponseDto();
        dto.setId(userId);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void getMe_whenNotFound_throwsUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getMe(userId));

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getMe_whenFound_returnsMappedDto() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto dto = new UserResponseDto();
        dto.setId(userId);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserResponseDto result = userService.getMe(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void register_whenEmailExists_throwsEmailAlreadyExists() {
        RegisterRequestDto req = new RegisterRequestDto();
        req.setEmail("exists@b.com");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(req));

        verify(userRepository, times(1)).existsByEmail(req.getEmail());
        verifyNoInteractions(passwordEncoder, userMapper);
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_happyPath_encodesPassword_savesAndReturnsDto() {
        RegisterRequestDto req = new RegisterRequestDto();
        req.setName("John");
        req.setSurname("Doe");
        req.setEmail("new@b.com");
        req.setPhoneNumber("+380991112233");
        req.setPassword("Password1");

        when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(req.getPassword())).thenReturn("ENC");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User saved = new User();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponseDto dto = new UserResponseDto();
        dto.setEmail(req.getEmail());
        when(userMapper.toDto(saved)).thenReturn(dto);

        UserResponseDto result = userService.register(req);

        assertNotNull(result);
        assertEquals(req.getEmail(), result.getEmail());

        verify(userRepository, times(1)).existsByEmail(req.getEmail());
        verify(passwordEncoder, times(1)).encode(req.getPassword());
        verify(userRepository, times(1)).save(userCaptor.capture());

        User created = userCaptor.getValue();
        assertEquals("John", created.getName());
        assertEquals("Doe", created.getSurname());
        assertEquals("new@b.com", created.getEmail());
        assertEquals("+380991112233", created.getPhoneNumber());
        assertEquals("ENC", created.getPassword());

        assertTrue(created.isActive());
        assertEquals(BigDecimal.ZERO, created.getBalance());
        assertEquals(UserRole.USER, created.getRole());
        assertNotNull(created.getOrders());
        assertTrue(created.getOrders().isEmpty());

        verify(userMapper, times(1)).toDto(saved);
    }

    @Test
    void createUser_whenEmailExists_throwsEmailAlreadyExists() {
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("exists@b.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(dto));

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verifyNoInteractions(passwordEncoder, userMapper);
        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_happyPath_encodesPassword_savesAndReturnsDto() {
        UserCreateDto createDto = new UserCreateDto(
                "Ann",
                "Lee",
                "ann@b.com",
                "+380991112233",
                "Password1",
                true,
                new BigDecimal("10.00"),
                UserRole.MANAGER
        );

        when(userRepository.existsByEmail(createDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(createDto.getPassword())).thenReturn("ENC2");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        User saved = new User();
        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserResponseDto resp = new UserResponseDto();
        resp.setEmail(createDto.getEmail());
        when(userMapper.toDto(saved)).thenReturn(resp);

        UserResponseDto result = userService.createUser(createDto);

        assertNotNull(result);
        assertEquals(createDto.getEmail(), result.getEmail());

        verify(userRepository, times(1)).existsByEmail(createDto.getEmail());
        verify(passwordEncoder, times(1)).encode(createDto.getPassword());
        verify(userRepository, times(1)).save(userCaptor.capture());

        User created = userCaptor.getValue();
        assertEquals("Ann", created.getName());
        assertEquals("Lee", created.getSurname());
        assertEquals("ann@b.com", created.getEmail());
        assertEquals("+380991112233", created.getPhoneNumber());
        assertEquals("ENC2", created.getPassword());
        assertTrue(created.isActive());
        assertEquals(new BigDecimal("10.00"), created.getBalance());
        assertEquals(UserRole.MANAGER, created.getRole());
        assertNotNull(created.getOrders());
        assertTrue(created.getOrders().isEmpty());

        verify(userMapper, times(1)).toDto(saved);
    }

    @Test
    void updateUser_whenNotFound_throwsUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateUser(userId, new UserAccessUpdateDto(true, UserRole.USER)));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
        verifyNoInteractions(userMapper);
    }

    @Test
    void updateUser_happyPath_callsMapperUpdate_savesAndReturnsDto() {
        UUID userId = UUID.randomUUID();
        User current = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(current));

        UserAccessUpdateDto upd = new UserAccessUpdateDto(false, UserRole.ADMIN);

        User saved = new User();
        when(userRepository.save(current)).thenReturn(saved);

        UserResponseDto resp = new UserResponseDto();
        resp.setId(userId);
        when(userMapper.toDto(saved)).thenReturn(resp);

        UserResponseDto result = userService.updateUser(userId, upd);

        assertNotNull(result);
        assertEquals(userId, result.getId());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).updateAccessFromDto(upd, current);
        verify(userRepository, times(1)).save(current);
        verify(userMapper, times(1)).toDto(saved);
    }

    @Test
    void updateProfile_whenUserNotFound_throwsUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.updateProfile(userId, new UserUpdateProfileDto()));

        verify(userRepository, times(1)).findById(userId);
        verifyNoInteractions(userMapper, passwordEncoder);
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateProfile_whenEmailChangedAndExists_throwsEmailAlreadyExists() {
        UUID userId = UUID.randomUUID();

        User current = new User();
        current.setEmail("old@b.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(current));

        UserUpdateProfileDto upd = new UserUpdateProfileDto();
        upd.setEmail("new@b.com");
        upd.setBalance(BigDecimal.ZERO);

        when(userRepository.existsByEmail("new@b.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class,
                () -> userService.updateProfile(userId, upd));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByEmail("new@b.com");
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).updateUserProfileFromDto(any(), any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateProfile_whenEmailChangedAndAvailable_setsEmail_andSaves() {
        UUID userId = UUID.randomUUID();

        User current = new User();
        current.setEmail("old@b.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(current));

        UserUpdateProfileDto upd = new UserUpdateProfileDto();
        upd.setEmail("new@b.com");
        upd.setBalance(BigDecimal.ZERO);

        when(userRepository.existsByEmail("new@b.com")).thenReturn(false);

        User saved = new User();
        when(userRepository.save(current)).thenReturn(saved);

        UserResponseDto resp = new UserResponseDto();
        resp.setEmail("new@b.com");
        when(userMapper.toDto(saved)).thenReturn(resp);

        UserResponseDto result = userService.updateProfile(userId, upd);

        assertNotNull(result);
        assertEquals("new@b.com", current.getEmail());
        assertEquals("new@b.com", result.getEmail());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByEmail("new@b.com");
        verify(userMapper, times(1)).updateUserProfileFromDto(upd, current);
        verify(userRepository, times(1)).save(current);
        verify(userMapper, times(1)).toDto(saved);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateProfile_whenPasswordProvided_encodesAndSetsPassword() {
        UUID userId = UUID.randomUUID();

        User current = new User();
        current.setEmail("same@b.com");
        current.setPassword("OLD");
        when(userRepository.findById(userId)).thenReturn(Optional.of(current));

        UserUpdateProfileDto upd = new UserUpdateProfileDto();
        upd.setEmail("same@b.com"); // not changed
        upd.setPassword("Password1");
        upd.setBalance(BigDecimal.ZERO);

        when(passwordEncoder.encode("Password1")).thenReturn("NEWENC");

        User saved = new User();
        when(userRepository.save(current)).thenReturn(saved);

        UserResponseDto resp = new UserResponseDto();
        when(userMapper.toDto(saved)).thenReturn(resp);

        userService.updateProfile(userId, upd);

        assertEquals("NEWENC", current.getPassword());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).updateUserProfileFromDto(upd, current);
        verify(passwordEncoder, times(1)).encode("Password1");
        verify(userRepository, times(1)).save(current);
    }

    @Test
    void updateProfile_whenPasswordBlank_doesNotEncode() {
        UUID userId = UUID.randomUUID();

        User current = new User();
        current.setEmail("same@b.com");
        current.setPassword("OLD");
        when(userRepository.findById(userId)).thenReturn(Optional.of(current));

        UserUpdateProfileDto upd = new UserUpdateProfileDto();
        upd.setEmail("same@b.com");
        upd.setPassword("   "); // blank
        upd.setBalance(BigDecimal.ZERO);

        User saved = new User();
        when(userRepository.save(current)).thenReturn(saved);

        UserResponseDto resp = new UserResponseDto();
        when(userMapper.toDto(saved)).thenReturn(resp);

        userService.updateProfile(userId, upd);

        assertEquals("OLD", current.getPassword());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).updateUserProfileFromDto(upd, current);
        verify(userRepository, times(1)).save(current);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void deleteUser_whenExists_deletes() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUser_whenNotExists_throwsUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}
