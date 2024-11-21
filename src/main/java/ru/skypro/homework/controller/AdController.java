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
import ru.skypro.homework.mappers.UserMapper;
import ru.skypro.homework.service.AdService;

import java.io.IOException;

/**
 * Контроллер для управления объявлениями.
 * Этот контроллер предоставляет API для получения, добавления, удаления, обновления и работы с изображениями объявлений.
 * Он использует сервисы {@link AdService} и {@link UserMapper} для обработки логики и преобразования данных.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class AdController {

    private final AdService adService;
    private final UserMapper userMapper;

    /**
     * Получает все объявления.
     *
     * @return {@link ResponseEntity} с объектом {@link Ads}, содержащим список всех объявлений и статусом {@code HttpStatus.OK}.
     */
    @GetMapping
    public ResponseEntity<Ads> getAllAdv() {
        Ads advertisements = adService.getAllAdv();
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    /**
     * Добавляет новое объявление.
     *
     * @param file             изображение, связанное с объявлением.
     * @param createOrUpdateAd объект с данными для создания или обновления объявления.
     * @param userDetails      данные о пользователе, создающем объявление.
     * @return {@link ResponseEntity} с созданным объектом {@link Ad} и статусом {@code HttpStatus.CREATED}.
     * @throws IOException если возникает ошибка при обработке файла изображения.
     */
    @PostMapping
    public ResponseEntity<Ad> addAdv(@RequestParam("image") MultipartFile file,
                                     @RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                     @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(adService.addAdv(file, createOrUpdateAd, userDetails.getUsername()), HttpStatus.CREATED);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExtendedAd> getAdvInfo(@PathVariable Integer id) {
        return new ResponseEntity<>(adService.getAdvInfo(id), HttpStatus.OK);
    }

    /**
     * Удаляет объявление по указанному идентификатору.
     *
     * @param id          идентификатор объявления для удаления.
     * @param userDetails данные о пользователе, удаляющем объявление.
     * @return {@link ResponseEntity} с статусом {@code HttpStatus.NO_CONTENT} после успешного удаления.
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteAdv(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        adService.deleteAdv(id, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Обновляет информацию об объявлении.
     *
     * @param id               идентификатор объявления для обновления.
     * @param createOrUpdateAd объект с обновленной информацией об объявлении.
     * @param userDetails      данные о пользователе, обновляющем объявление.
     * @return {@link ResponseEntity} с обновленным объектом {@link Ad} и статусом {@code HttpStatus.CREATED}.
     */
    @PatchMapping(path = "/{id}")
    public ResponseEntity<Ad> patchAdvInfo(@PathVariable Integer id, @RequestBody CreateOrUpdateAd createOrUpdateAd, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(adService.updateAdvInfo(id, createOrUpdateAd, userDetails.getUsername()), HttpStatus.CREATED);
    }

    /**
     * Получает все объявления текущего пользователя.
     *
     * @param userDetails данные о текущем пользователе.
     * @return {@link ResponseEntity} с объектом {@link Ads}, содержащим объявления пользователя, и статусом {@code HttpStatus.OK}.
     */
    @GetMapping(path = "/me")
    public ResponseEntity<Ads> getAdvCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(adService.getAdvCurrentUser(userDetails.getUsername()), HttpStatus.OK);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id          идентификатор объявления для обновления изображения.
     * @param file        новое изображение.
     * @param userDetails данные о пользователе, обновляющем изображение.
     * @return {@link ResponseEntity} с обновленным изображением и статусом {@code HttpStatus.OK}.
     * @throws IOException если возникает ошибка при обработке файла изображения.
     */
    @PatchMapping(path = "/{id}/image")
    public ResponseEntity<byte[]> patchAdvImage(@PathVariable Integer id,
                                                @RequestParam MultipartFile file,
                                                @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (file.getSize() > 50_000_000) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(adService.updateAdvImage(id, file, userDetails.getUsername()));
    }

}
