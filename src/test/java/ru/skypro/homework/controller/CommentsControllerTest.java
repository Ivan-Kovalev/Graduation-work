package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentsControllerTest {

    @InjectMocks
    private CommentsController commentsController;

    @Mock
    private CommentService service;

    @Test
    void getCommentsAdv_ShouldReturnOk() {
        Integer adId = 1;
        Comments comments = new Comments(2, new Comment[]{
                new Comment(1, "image1.jpg", "John", 1623400000, "Great ad!", 1),
                new Comment(2, "image2.jpg", "Jane", 1623400001, "Nice product", 2)
        });
        when(service.getCommentsAdv(adId)).thenReturn(comments);
        ResponseEntity<Comments> response = commentsController.getCommentsAdv(adId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCount());
        assertEquals("Great ad!", response.getBody().getResults()[0].getText());
    }

    @Test
    void addCommentAdv_ShouldReturnOk() {
        Integer adId = 1;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("This is a new comment");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        Comment createdComment = new Comment(1, "image.jpg", "John", 1623400000, "This is a new comment", 1);
        when(service.addCommentAdv(adId, createOrUpdateComment, "username")).thenReturn(createdComment);
        ResponseEntity<Comment> response = commentsController.addCommentAdv(adId, createOrUpdateComment, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("This is a new comment", response.getBody().getText());
    }

    @Test
    void deleteCommentAdv_ShouldReturnOk() {
        Integer adId = 1;
        Integer commentId = 1;
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        doNothing().when(service).deleteCommentAdv(adId, commentId, "username");
        ResponseEntity<HttpStatus> response = commentsController.deleteCommentAdv(adId, commentId, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).deleteCommentAdv(adId, commentId, "username");
    }

}

