package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;

@Mapper
public interface CommentMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    Comment mapCommentToCommentEntity(CommentEntity commentEntity);

    CommentEntity mapCommentEntityToComment(Comment comment);

    CommentEntity mapCreateOrUpdateCommentToCommentEntity(CreateOrUpdateComment createOrUpdateComment);

    CreateOrUpdateComment mapCreateOrUpdateCommentToCommentEntity(CommentEntity commentEntity);

}
