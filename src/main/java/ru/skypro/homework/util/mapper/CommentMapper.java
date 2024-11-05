package ru.skypro.homework.util.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

@Component
public class CommentMapper {

    public CommentEntity mapCommentEntityToComment(Comment comment) {
        return new CommentEntity(
                new UserEntity(comment.getAuthor()),
                comment.getAuthorImage(),
                comment.getAuthorFirstName(),
                comment.getCreatedAt(),
                comment.getPk(),
                comment.getText()
        );
    }

    public Comment mapCommentToCommentEntity(CommentEntity commentEntity) {
        return new Comment(
                commentEntity.getAuthor().getId(),
                commentEntity.getAuthorImage(),
                commentEntity.getAuthorFirstName(),
                commentEntity.getCreatedAt(),
                commentEntity.getPk(),
                commentEntity.getText()
        );
    }

    public CommentEntity mapCreateOrUpdateCommentToCommentEntity(CreateOrUpdateComment createOrUpdateComment) {
        return new CommentEntity(
                createOrUpdateComment.getText()
        );
    }

    public CreateOrUpdateComment mapCreateOrUpdateCommentToCommentEntity(CommentEntity commentEntity) {
        return new CreateOrUpdateComment(
                commentEntity.getText()
        );
    }



}
