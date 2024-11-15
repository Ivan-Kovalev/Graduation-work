package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.ForbiddenActionException;
import ru.skypro.homework.mappers.AdMapper;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private CommentMapper commentMapper;
    private AdMapper adMapper;

    @Override
    public Comments getCommentsAdv(Integer id) {
        AdEntity adEntity = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException("Ошибка! Реклама с данным id не найдена"));
        List<Comment> comments = adEntity.getComments().stream().map(this::mapCommentEntityToComment).collect(Collectors.toList());
        return new Comments(comments.size(), comments.toArray(new Comment[0]));
    }

    private Comment mapCommentEntityToComment(CommentEntity commentEntity) {
        return commentMapper.mapCommentEntityToComment(commentEntity);
    }

    @Override
    public Comment addCommentAdv(Integer id, CreateOrUpdateComment createOrUpdateComment, String username) {
        UserEntity user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Ошибка! Пользователь не найден!"));
        AdEntity ad = adRepository.findAdEntityByPk(id);
        CommentEntity comment = new CommentEntity(user.getImage(), user.getFirstName(), id, createOrUpdateComment.getText(), ad, user);
        return commentMapper.mapCommentEntityToComment(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentAdv(Integer adId, Integer commentId, String username) {
        CommentEntity comment = commentRepository.findById(adId).orElseThrow(() -> new CommentNotFoundException("Ошибка! Комментарий с указанным ID не найден"));
        boolean isAuthor = username.equals(comment.getAuthor().getEmail());
        boolean isAdmin = comment.getAuthor().getRole().equals(Role.ADMIN);
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenActionException("Пользователь " + username + " не имеет прав удалять чужие комментарии.");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateCommentAdv(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment, String username) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("Ошибка! Комментарий с указанным ID не найден"));
        boolean isAuthor = username.equals(comment.getAuthor().getEmail());
        boolean isAdmin = comment.getAuthor().getRole().equals(Role.ADMIN);
        if (!isAuthor && !isAdmin) {
            throw new ForbiddenActionException("Пользователь " + username + " не имеет прав удалять чужие комментарии.");
        }
        comment.setText(createOrUpdateComment.getText());
        commentRepository.save(comment);
        return commentMapper.mapCommentEntityToComment(comment);
    }
}
