package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateAd;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ads")
public class CommentsController {

    @GetMapping(path = "/{id}/comments")
    public ResponseEntity<Comments> getCommentsAdv(@PathVariable Integer id) {
        Comment[] comments = new Comment[1];
        comments[0] = new Comment(0, "", "Валера", 10000, 101, "Текст комментария");
        return new ResponseEntity<>(new Comments(comments.length, comments), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/comments")
    public ResponseEntity<Comment> addCommentAdv(@PathVariable Integer id,
                                                 @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return new ResponseEntity<>(new Comment(1, "", "Author", 1000, 1000, "Text"), HttpStatus.OK);
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
