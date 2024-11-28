package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.ForbiddenActionException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AdMapper adMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private UserEntity user;
    private AdEntity ad;

    @BeforeEach
    void setUp() {
        user = new UserEntity(1, "user@example.com", "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        ad = new AdEntity();
        ad.setPk(1);
    }

    @Test
    void getCommentsAdv_ShouldReturnComments_WhenAdExists() {
        int adId = 1;
        CommentEntity comment = new CommentEntity();
        comment.setText("Great ad!");
        AdEntity ad = new AdEntity();
        ad.setPk(adId);
        ad.setComments(List.of(comment));
        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        Comment mockComment = new Comment(1, "image.png", "John", 123456789, "Great ad!", 1);
        when(commentMapper.mapCommentEntityToComment(comment)).thenReturn(mockComment);
        Comments comments = commentService.getCommentsAdv(adId);
        assertNotNull(comments);
        assertEquals(1, comments.getCount());
        assertEquals("Great ad!", comments.getResults()[0].getText());
    }


    @Test
    void addCommentAdv_ShouldAddComment_WhenUserExistsAndAdExists() {
        String username = "user@example.com";
        int adId = 1;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment("Great ad!");
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(adRepository.findAdEntityByPk(adId)).thenReturn(ad);
        CommentEntity commentEntity = new CommentEntity(user.getImage(), user.getFirstName(), adId, createOrUpdateComment.getText(), ad, user);
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);
        when(commentMapper.mapCommentEntityToComment(any(CommentEntity.class))).thenReturn(new Comment(1, user.getImage(), user.getFirstName(), 123456789, "Great ad!", user.getId()));
        Comment comment = commentService.addCommentAdv(adId, createOrUpdateComment, username);
        assertNotNull(comment);
        assertEquals("Great ad!", comment.getText());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void deleteCommentAdv_ShouldDeleteComment_WhenUserIsAuthor() {
        int adId = 1;
        int commentId = 1;
        String username = "author@example.com";
        UserEntity author = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        CommentEntity comment = new CommentEntity();
        comment.setAuthor(author);
        comment.setPk(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        commentService.deleteCommentAdv(adId, commentId, username);
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void deleteCommentAdv_ShouldThrowForbiddenActionException_WhenUserIsNotAuthorNorAdmin() {
        int adId = 1;
        int commentId = 1;
        String username = "user@example.com";

        UserEntity author = new UserEntity(1, "author@example.com", "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        CommentEntity comment = new CommentEntity();
        comment.setAuthor(author);
        comment.setPk(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        ForbiddenActionException exception = assertThrows(ForbiddenActionException.class, () -> {
            commentService.deleteCommentAdv(adId, commentId, username);
        });
        assertEquals("Пользователь user@example.com не имеет прав удалять чужие комментарии.", exception.getMessage());
    }

    @Test
    void updateCommentAdv_ShouldUpdateComment_WhenUserIsAuthor() {
        int adId = 1;
        int commentId = 1;
        String username = "author@example.com";
        CreateOrUpdateComment updateComment = new CreateOrUpdateComment("Updated text");
        UserEntity author = new UserEntity(1, username, "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        CommentEntity comment = new CommentEntity();
        comment.setPk(commentId);
        comment.setText("Old text");
        comment.setAuthor(author);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(comment);
        when(commentMapper.mapCommentEntityToComment(any(CommentEntity.class))).thenReturn(new Comment(commentId, author.getImage(), author.getFirstName(), 123456789, "Updated text", author.getId()));
        Comment updatedComment = commentService.updateCommentAdv(adId, commentId, updateComment, username);
        assertEquals("Updated text", updatedComment.getText());
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void updateCommentAdv_ShouldThrowForbiddenActionException_WhenUserIsNotAuthorNorAdmin() {
        int adId = 1;
        int commentId = 1;
        String username = "user@example.com";
        UserEntity author = new UserEntity(1, "author@example.com", "password", "John", "Doe", "1234567890", Role.USER, null, null, null);
        CommentEntity comment = new CommentEntity();
        comment.setAuthor(author);
        comment.setPk(commentId);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        ForbiddenActionException exception = assertThrows(ForbiddenActionException.class, () -> {
            commentService.updateCommentAdv(adId, commentId, new CreateOrUpdateComment("Updated text"), username);
        });
        assertEquals("Пользователь user@example.com не имеет прав удалять чужие комментарии.", exception.getMessage());
    }
}

