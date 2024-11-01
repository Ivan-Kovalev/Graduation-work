package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class AdvertisementsController {

    @GetMapping
    public ResponseEntity<HttpStatus> getAllAdv() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addAdv(@RequestBody MultipartFile file,
                                             @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> getAdvInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteAdv(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> patchAdvInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<HttpStatus> getAdvCurrentUser() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/image")
    public ResponseEntity<HttpStatus> patchAdvImage(@PathVariable Integer id, @RequestBody MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (file.getSize() > 50_000) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
