package ru.skypro.homework.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper( CommentMapper.class );

    @Mapping(target = "author", source = "author.id")
    Comment mapCommentEntityToComment(CommentEntity commentEntity);

    @Mapping(source = "author", target = "author.id")
    CommentEntity mapCommentToCommentEntity(Comment comment);

    CommentEntity mapCreateOrUpdateCommentToCommentEntity(CreateOrUpdateComment createOrUpdateComment);

    CreateOrUpdateComment mapCommentEntityToCreateOrUpdateComment(CommentEntity commentEntity);

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
