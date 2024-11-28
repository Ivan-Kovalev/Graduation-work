package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 * Этот контроллер предоставляет API для входа пользователя в систему и регистрации нового пользователя.
 * Он использует сервис {@link AuthService} для выполнения аутентификации и регистрации.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param login объект, содержащий имя пользователя и пароль для входа.
     * @return {@link ResponseEntity} с кодом ответа {@code HttpStatus.OK}, если аутентификация успешна,
     * или {@code HttpStatus.UNAUTHORIZED}, если аутентификация не удалась.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register объект, содержащий информацию для регистрации нового пользователя.
     * @return {@link ResponseEntity} с кодом ответа {@code HttpStatus.CREATED}, если регистрация успешна,
     * или {@code HttpStatus.BAD_REQUEST}, если регистрация не удалась.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
