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

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UsersController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (!newPassword.getNewPassword().equals(newPassword.getCurrentPassword())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newPassword.getNewPassword().length() > 16 || newPassword.getNewPassword().length() < 8) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            newPassword.setNewPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            newPassword.setCurrentPassword(passwordEncoder.encode(newPassword.getCurrentPassword()));
            userService.setPassword(newPassword, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> getCurrentUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(userService.getCurrentUserInfo(userDetails.getUsername()), HttpStatus.OK);
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUser> patchCurrentUserInfo(@RequestBody UpdateUser updateUser,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(userService.patchCurrentUserInfo(updateUser, userDetails.getUsername()), HttpStatus.OK);
        }
    }

    @PatchMapping(path = "/me/image")
    public ResponseEntity<HttpStatus> patchCurrentUserImage(@RequestBody MultipartFile file,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            userService.patchCurrentUserImage(file, userDetails.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
