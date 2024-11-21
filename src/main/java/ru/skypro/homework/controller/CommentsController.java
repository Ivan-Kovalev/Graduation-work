package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

/**
 * Контроллер для работы с комментариями к объявлениям.
 * Этот контроллер предоставляет API для получения, добавления, удаления и обновления комментариев для объявлений.
 * Он использует сервис {@link CommentService} для выполнения операций с комментариями.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class CommentsController {

    private final CommentService service;

    /**
     * Получает комментарии для объявления по его ID.
     *
     * @param id идентификатор объявления.
     * @return {@link ResponseEntity} с объектом {@link Comments}, содержащим все комментарии для объявления,
     *         с кодом ответа {@code HttpStatus.OK}.
     */
    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<Comments> getCommentsAdv(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getCommentsAdv(id), HttpStatus.OK);
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param id идентификатор объявления.
     * @param createOrUpdateComment объект, содержащий текст комментария для добавления.
     * @param userDetails детали аутентифицированного пользователя.
     * @return {@link ResponseEntity} с добавленным объектом {@link Comment}, с кодом ответа {@code HttpStatus.OK}.
     */
    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Comment> addCommentAdv(@PathVariable Integer id,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(service.addCommentAdv(id, createOrUpdateComment, userDetails.getUsername()), HttpStatus.OK);
    }

    /**
     * Удаляет комментарий к объявлению по его ID.
     *
     * @param adId идентификатор объявления.
     * @param commentId идентификатор комментария для удаления.
     * @param userDetails детали аутентифицированного пользователя.
     * @return {@link ResponseEntity} с кодом ответа {@code HttpStatus.OK} после успешного удаления комментария.
     */
    @DeleteMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> deleteCommentAdv(@PathVariable Integer adId,
                                                       @PathVariable Integer commentId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteCommentAdv(adId, commentId, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Обновляет текст комментария к объявлению.
     *
     * @param adId идентификатор объявления.
     * @param commentId идентификатор комментария для обновления.
     * @param createOrUpdateComment объект, содержащий новый текст комментария.
     * @param userDetails детали аутентифицированного пользователя.
     * @return {@link ResponseEntity} с кодом ответа {@code HttpStatus.OK} после успешного обновления комментария.
     */
    @PatchMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> patchCommentAdv(@PathVariable Integer adId,
                                                   @PathVariable Integer commentId,
                                                   @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        service.updateCommentAdv(adId, commentId, createOrUpdateComment, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
