package ru.skypro.homework.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    Comments getCommentsAdv(Integer id);

    Comment addCommentAdv(Integer id, CreateOrUpdateComment createOrUpdateComment, UserDetails userDetails);

    void deleteCommentAdv(Integer adId, Integer commentId, UserDetails userDetails);

    Comment patchCommentAdv(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment, UserDetails userDetails);
}
