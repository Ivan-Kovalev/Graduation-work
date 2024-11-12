package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserService service;

    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        if (!newPassword.getNewPassword().equals(newPassword.getCurrentPassword())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newPassword.getNewPassword().length() > 16 || newPassword.getNewPassword().length() < 8) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            service.setPassword(newPassword, userDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<User> getCurrentUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(service.getCurrentUserInfo(userDetails), HttpStatus.OK);
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<UpdateUser> patchCurrentUserInfo(@RequestBody UpdateUser updateUser,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(service.patchCurrentUserInfo(updateUser, userDetails), HttpStatus.OK);
        }
    }

    @PatchMapping(path = "/me/image")
    public ResponseEntity<HttpStatus> patchCurrentUserImage(@RequestBody MultipartFile file,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            service.patchCurrentUserImage(file, userDetails);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
