package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class AdController {

    @GetMapping
    public ResponseEntity<Ads> getAllAdv() {
        Ad[] advertisements = new Ad[1];
        advertisements[0] = new Ad(0, "", 10, 10000, "TestTitle");
        return new ResponseEntity<>(new Ads(advertisements.length, advertisements), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ad> addAdv(@RequestBody MultipartFile file,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(new Ad(0, "", 10, 10000, "TestTitle"), HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExtendedAd> getAdvInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(new ExtendedAd(1, "Firstname", "Lastname", "Description", "email@email.ru", "", "88005353535", 10000, "TestTitle"), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteAdv(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Ad> patchAdvInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(new Ad(0, "", 10, 10000, "TestTitle"), HttpStatus.CREATED);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<Ads> getAdvCurrentUser() {
        Ad[] advertisements = new Ad[1];
        advertisements[0] = new Ad(0, "", 10, 10000, "TestTitle");
        return new ResponseEntity<>(new Ads(advertisements.length, advertisements), HttpStatus.OK);
    }

    @PatchMapping(path = "/{id}/image")
    public ResponseEntity<byte[]> patchAdvImage(@PathVariable Integer id,
                                                @RequestParam MultipartFile file,
                                                @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (file.getSize() > 50_000) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        byte[] updatedImageContent = file.getBytes();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(updatedImageContent);
    }

}
