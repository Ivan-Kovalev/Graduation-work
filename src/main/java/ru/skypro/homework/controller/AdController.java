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
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdvertisementsService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class AdController {

    private final AdvertisementsService advertisementsService;

    @GetMapping
    public ResponseEntity<Ads> getAllAdv() {
        Ads advertisements = advertisementsService.getAllAdv();
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ad> addAdv(@RequestParam("image") MultipartFile file,
                                     @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                     @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(advertisementsService.addAdv(file, createOrUpdateAd, userDetails.getUsername()), HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExtendedAd> getAdvInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(advertisementsService.getAdvInfo(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteAdv(@PathVariable Integer id) {
        advertisementsService.deleteAdv(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<Ad> patchAdvInfo(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new ResponseEntity<>(advertisementsService.patchAdvInfo(id, createOrUpdateAd), HttpStatus.CREATED);
    }

    @GetMapping(path = "/me")
    public ResponseEntity<Ads> getAdvCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(advertisementsService.getAdvCurrentUser(userDetails.getUsername()), HttpStatus.OK);
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
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(advertisementsService.patchAdvImage(id, file, userDetails.getUsername()));
    }

}
