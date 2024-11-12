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
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class CommentsController {

    private final CommentService service;

    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<Comments> getCommentsAdv(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getCommentsAdv(id), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Comment> addCommentAdv(@PathVariable Integer id,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(service.addCommentAdv(id, createOrUpdateComment, userDetails.getUsername()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> deleteCommentAdv(@PathVariable Integer adId,
                                                       @PathVariable Integer commentId,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteCommentAdv(adId, commentId, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> patchCommentAdv(@PathVariable Integer adId,
                                                   @PathVariable Integer commentId,
                                                   @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        service.patchCommentAdv(adId, commentId, createOrUpdateComment, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
