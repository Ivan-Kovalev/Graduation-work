package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UsersController {

    @PostMapping(path = "/set_password")
    public ResponseEntity<HttpStatus> setPassword(@RequestBody NewPassword newPassword) {
        if (!newPassword.getNewPassword().equals(newPassword.getCurrentPassword())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newPassword.getNewPassword().length() > 16 || newPassword.getNewPassword().length() < 8) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping(path = "/me")
    public ResponseEntity<HttpStatus> getCurrentUserInfo() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/me")
    public ResponseEntity<HttpStatus> patchCurrentUserInfo(@RequestBody UpdateUser updateUser) {
        if (updateUser.getPhone() == null || updateUser.getFirstName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PatchMapping(path = "/me/image")
    public ResponseEntity<HttpStatus> patchCurrentUserImage(@RequestBody MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
