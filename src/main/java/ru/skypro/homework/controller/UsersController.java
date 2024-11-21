package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;

/**
 * Контроллер для работы с данными пользователей.
 * Этот контроллер предоставляет API для обновления информации о пользователе, изменения пароля и работы с изображениями пользователя.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UsersController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Изменяет пароль пользователя.
     *
     * @param newPassword объект, содержащий текущий и новый пароль пользователя.
     * @param userDetails объект, содержащий данные аутентифицированного пользователя.
     * @return {@link ResponseEntity} с кодом состояния {@code HttpStatus.OK}, если пароль успешно изменен,
     * {@code HttpStatus.UNAUTHORIZED}, если пользователь не аутентифицирован,
     * или {@code HttpStatus.FORBIDDEN}, если текущий пароль неверный.
     */
    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!passwordEncoder.matches(newPassword.getCurrentPassword(), userDetails.getPassword())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            newPassword.setNewPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            userService.setPassword(newPassword, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Получает информацию о текущем пользователе.
     *
     * @param userDetails объект, содержащий данные аутентифицированного пользователя.
     * @return {@link ResponseEntity} с данными пользователя в теле ответа и кодом состояния {@code HttpStatus.OK}.
     */
    @GetMapping(path = "/me")
    public ResponseEntity<User> getCurrentUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userService.getCurrentUserInfo(userDetails.getUsername()), HttpStatus.OK);
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param updateUser  объект, содержащий обновленные данные пользователя.
     * @param userDetails объект, содержащий данные аутентифицированного пользователя.
     * @return {@link ResponseEntity} с обновленной информацией о пользователе и кодом состояния {@code HttpStatus.OK},
     * или {@code HttpStatus.UNAUTHORIZED}, если пользователь не аутентифицирован.
     */
    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUser> updateCurrentUserInfo(@RequestBody UpdateUser updateUser,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(userService.updateCurrentUserInfo(updateUser, userDetails.getUsername()), HttpStatus.OK);
        }
    }

    /**
     * Обновляет изображение пользователя.
     *
     * @param file        изображение пользователя, которое необходимо обновить.
     * @param userDetails объект, содержащий данные аутентифицированного пользователя.
     * @return {@link ResponseEntity} с кодом состояния {@code HttpStatus.OK}, если изображение успешно обновлено,
     * или {@code HttpStatus.UNAUTHORIZED}, если пользователь не аутентифицирован.
     */
    @PatchMapping(path = "/me/image")
    public ResponseEntity<HttpStatus> updateCurrentUserImage(@RequestParam("image") MultipartFile file,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            userService.updateCurrentUserImage(file, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
