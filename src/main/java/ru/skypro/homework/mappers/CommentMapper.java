package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

@Mapper
public interface CommentMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    @Mapping(target = "author", expression = "java(commentEntity.getAuthor().getId())")
    Comment mapCommentEntityToComment (CommentEntity commentEntity);

    @Mapping(source = "author", target = "author", qualifiedByName = "mapIntegerToUserEntity")
    CommentEntity mapCommentToCommentEntity(Comment comment);

    CommentEntity mapCreateOrUpdateCommentToCommentEntity(CreateOrUpdateComment createOrUpdateComment);

    CreateOrUpdateComment mapCreateOrUpdateCommentToCommentEntity(CommentEntity commentEntity);

    @Named("mapIntegerToUserEntity")
    default UserEntity map(Integer value) {
        if (value == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(value);
        return userEntity;
    }
}
