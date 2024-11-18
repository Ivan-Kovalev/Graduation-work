package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testLoginSuccess() {
        Login login = new Login("user", "password");
        when(authService.login("user", "password")).thenReturn(true);
        ResponseEntity<?> response = authController.login(login);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginFailure() {
        // Подготовка
        Login login = new Login("user", "wrongpassword");
        when(authService.login("user", "wrongpassword")).thenReturn(false);
        ResponseEntity<?> response = authController.login(login);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testRegisterSuccess() {
        // Подготовка
        Register register = new Register("user", "password", "John", "Doe", "1234567890", Role.USER);
        when(authService.register(register)).thenReturn(true);
        ResponseEntity<?> response = authController.register(register);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testRegisterFailure() {
        Register register = new Register("user", "password", "John", "Doe", "1234567890", Role.USER);
        when(authService.register(register)).thenReturn(false);
        ResponseEntity<?> response = authController.register(register);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
