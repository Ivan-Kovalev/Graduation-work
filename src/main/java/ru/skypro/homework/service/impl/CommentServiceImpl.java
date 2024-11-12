package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mappers.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private CommentMapper mapper;

    @Override
    public Comments getCommentsAdv(Integer id) {
        AdEntity adEntity = adRepository.findAdEntityByPk(id);
        return new Comments(adEntity.getComments().size(), (Comment[]) adEntity.getComments().toArray());
    }

    @Override
    public Comment addCommentAdv(Integer id, CreateOrUpdateComment createOrUpdateComment, UserDetails userDetails) {
        UserEntity user = userRepository.findUserEntitiesByEmail(userDetails.getUsername());
        AdEntity ad = adRepository.findAdEntityByPk(id);
        CommentEntity comment = new CommentEntity(user.getImage(), user.getFirstName(), id, createOrUpdateComment.getText(), ad, user);
        return mapper.mapCommentEntityToComment(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentAdv(Integer adId, Integer commentId, UserDetails userDetails) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment patchCommentAdv(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment, UserDetails userDetails) {
        UserEntity user = userRepository.findUserEntitiesByEmail(userDetails.getUsername());
        AdEntity ad = adRepository.findAdEntityByPk(adId);
        CommentEntity comment = new CommentEntity(user.getImage(), user.getFirstName(), commentId, createOrUpdateComment.getText(), ad, user);
        commentRepository.updateCommentEntityByPk(commentId, comment);
        return mapper.mapCommentEntityToComment(comment);
    }
}
