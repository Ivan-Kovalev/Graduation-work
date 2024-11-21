package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.util.FileService;

import java.io.FileNotFoundException;

/**
 * Контроллер для работы с изображениями предварительного просмотра объявлений.
 * Этот контроллер предоставляет API для получения изображений по имени файла.
 * Он использует сервис {@link FileService} для загрузки и отправки файлов.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/images/ad/preview")
@RequiredArgsConstructor
public class ImageController {

    private final FileService fileService;

    /**
     * Загружает изображение по его имени.
     *
     * @param fileName имя файла изображения, который требуется загрузить.
     * @return {@link ResponseEntity} с ресурсом изображения в теле ответа и кодом состояния {@code HttpStatus.OK},
     * если файл найден. В противном случае — код состояния {@code HttpStatus.NOT_FOUND}.
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        try {
            Resource resource = fileService.getFile(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
