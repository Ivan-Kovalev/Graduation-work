package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.UserIsAlreadyExistException;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Test
    void login_ShouldReturnTrue_WhenCredentialsAreValid() {
        String username = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity(1, username, "$2a$10$encodedPassword", "John", "Doe", "1234567890", Role.USER, null, null, null);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(username);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @Test
    void login_ShouldReturnFalse_WhenUserDoesNotExist() {
        // Arrange
        String username = "nonexistent@example.com";
        String password = "password";

        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(username);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void login_ShouldReturnFalse_WhenPasswordIsInvalid() {
        // Arrange
        String username = "test@example.com";
        String password = "wrongPassword";
        UserEntity user = new UserEntity(1, username, "$2a$10$encodedPassword", "John", "Doe", "1234567890", Role.USER, null, null, null);

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).findByEmail(username);
        verify(passwordEncoder, times(1)).matches(password, user.getPassword());
    }

    @Test
    void register_ShouldThrowException_WhenUserAlreadyExists() {
        // Arrange
        Register register = new Register("test@example.com", "password", "John", "Doe", "1234567890", Role.USER);
        when(userRepository.findByEmail(register.getUsername())).thenReturn(Optional.of(new UserEntity()));

        // Act & Assert
        assertThrows(UserIsAlreadyExistException.class, () -> authService.register(register));
        verify(userRepository, times(1)).findByEmail(register.getUsername());
        verifyNoInteractions(passwordEncoder, userMapper);
    }

    @Test
    void register_ShouldSaveUser_WhenUserDoesNotExist() {
        String password = "password";
        Register register = new Register("test@example.com", password, "John", "Doe", "1234567890", Role.USER);
        UserEntity userEntity = new UserEntity(1, register.getUsername(), "encodedPassword", register.getFirstName(), register.getLastName(), register.getPhone(), register.getRole(), null, null, null);
        when(userRepository.findByEmail(register.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userMapper.mapRegisterToUserEntity(register)).thenReturn(userEntity);
        boolean result = authService.register(register);
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(register.getUsername());
        verify(passwordEncoder, times(1)).encode(password);
        verify(userMapper, times(1)).mapRegisterToUserEntity(register);
        verify(userRepository, times(1)).save(userEntity);
    }


}
