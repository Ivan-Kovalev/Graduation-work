package ru.skypro.homework.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersController usersController;

    private UserDetails userDetails;
    private NewPassword newPassword;

    @BeforeEach
    void setUp() {
        userDetails = mock(UserDetails.class);
        newPassword = new NewPassword("oldPassword", "newPassword");
    }

    @Test
    void setPassword_ShouldReturnUnauthorized_WhenUserNotAuthenticated() {
        ResponseEntity<HttpStatus> response = usersController.setPassword(newPassword, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void setPassword_ShouldReturnForbidden_WhenCurrentPasswordIncorrect() {
        when(userDetails.getPassword()).thenReturn("encodedOldPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        ResponseEntity<HttpStatus> response = usersController.setPassword(newPassword, userDetails);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void setPassword_ShouldReturnOk_WhenPasswordSuccessfullyChanged() {
        when(userDetails.getPassword()).thenReturn("encodedOldPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(newPassword.getNewPassword())).thenReturn("encodedNewPassword");
        ResponseEntity<HttpStatus> response = usersController.setPassword(newPassword, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).setPassword(newPassword, userDetails.getUsername());
    }

    @Test
    void getCurrentUserInfo_ShouldReturnUserInfo_WhenUserIsAuthenticated() {
        User mockUser = new User(1, "test@test.com", "First", "Last", "1234567890", "ROLE_USER", "image.jpg");
        when(userDetails.getUsername()).thenReturn("test@test.com");
        when(userService.getCurrentUserInfo("test@test.com")).thenReturn(mockUser);
        ResponseEntity<User> response = usersController.getCurrentUserInfo(userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void updateCurrentUserInfo_ShouldReturnUnauthorized_WhenUserNotAuthenticated() {
        UpdateUser updateUser = new UpdateUser("NewFirst", "NewLast", "1234567890");
        ResponseEntity<UpdateUser> response = usersController.updateCurrentUserInfo(updateUser, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateCurrentUserInfo_ShouldReturnOk_WhenUserIsAuthenticated() {
        when(userDetails.getUsername()).thenReturn("test@test.com");
        UpdateUser updateUser = new UpdateUser("NewFirst", "NewLast", "1234567890");
        when(userService.updateCurrentUserInfo(updateUser, "test@test.com")).thenReturn(updateUser);
        ResponseEntity<UpdateUser> response = usersController.updateCurrentUserInfo(updateUser, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updateUser, response.getBody());
    }

    @Test
    void updateCurrentUserImage_ShouldReturnUnauthorized_WhenUserNotAuthenticated() {
        MultipartFile file = mock(MultipartFile.class);
        ResponseEntity<HttpStatus> response = usersController.updateCurrentUserImage(file, null);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void updateCurrentUserImage_ShouldReturnOk_WhenUserIsAuthenticated() {
        when(userDetails.getUsername()).thenReturn("test@test.com");
        MultipartFile file = mock(MultipartFile.class);
        ResponseEntity<HttpStatus> response = usersController.updateCurrentUserImage(file, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateCurrentUserImage(file, "test@test.com");
    }
}

