package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateAd;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class CommentsController {

    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<HttpStatus> getCommentsAdv(@PathVariable Integer id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Comment> addCommentAdv(@PathVariable Integer id,
                                                 @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<HttpStatus> deleteCommentAdv(@PathVariable Integer adId,
                                                       @PathVariable Integer commentId) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> patchCommentAdv(@PathVariable Integer adId,
                                                   @PathVariable Integer commentId,
                                                   @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
