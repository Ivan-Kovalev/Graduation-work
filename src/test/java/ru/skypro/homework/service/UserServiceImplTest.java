package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;
import ru.skypro.homework.util.FileService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileService fileService;

    @Mock
    private UserMapper mapper;

    @Test
    void setPassword_ShouldSetNewPassword_WhenUserExists() {
        String username = "user@example.com";
        NewPassword newPassword = new NewPassword("oldPassword", "newPassword");
        UserEntity userEntity = new UserEntity(1, username, "oldPassword", "John", "Doe", "1234567890", Role.USER, null, null, null);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        userService.setPassword(newPassword, username);
        assertEquals("newPassword", userEntity.getPassword());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void getCurrentUserInfo_ShouldReturnUserInfo_WhenUserExists() {
        String username = "user@example.com";
        UserEntity userEntity = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, "image.png", null, null);
        User expectedUser = new User(1, username, "John", "Doe", "1234567890", "USER", "image.png");
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(mapper.mapUserEntityToUser(userEntity)).thenReturn(expectedUser);
        User actualUser = userService.getCurrentUserInfo(username);
        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void updateCurrentUserInfo_ShouldUpdateUserInfo_WhenUserExists() {
        // Arrange
        String username = "user@example.com";
        UpdateUser updateUser = new UpdateUser("NewFirstName", "NewLastName", "9876543210");
        UserEntity userEntity = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.mapUserEntityToUpdateUser(userEntity)).thenReturn(updateUser);
        UpdateUser updatedUser = userService.updateCurrentUserInfo(updateUser, username);
        assertEquals("NewFirstName", userEntity.getFirstName());
        assertEquals("NewLastName", userEntity.getLastName());
        assertEquals("9876543210", userEntity.getPhone());
        assertEquals(updateUser, updatedUser);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updateCurrentUserImage_ShouldUpdateUserImage_WhenUserExists() {
        String username = "user@example.com";
        MultipartFile file = mock(MultipartFile.class);
        UserEntity userEntity = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, "old_image.png", null, null);
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(userEntity));
        when(fileService.updateFile(anyString(), eq(file))).thenReturn("new_image.png");
        userService.updateCurrentUserImage(file, username);
        assertEquals("new_image.png", userEntity.getImage());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void setPassword_ShouldThrowException_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        NewPassword newPassword = new NewPassword("oldPassword", "newPassword");
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.setPassword(newPassword, username));
    }

    @Test
    void getCurrentUserInfo_ShouldThrowException_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getCurrentUserInfo(username));
    }

    @Test
    void updateCurrentUserInfo_ShouldThrowException_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        UpdateUser updateUser = new UpdateUser("NewFirstName", "NewLastName", "9876543210");
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.updateCurrentUserInfo(updateUser, username));
    }

    @Test
    void updateCurrentUserImage_ShouldThrowException_WhenUserNotFound() {
        String username = "nonexistent@example.com";
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findByEmail(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.updateCurrentUserImage(file, username));
    }
}


