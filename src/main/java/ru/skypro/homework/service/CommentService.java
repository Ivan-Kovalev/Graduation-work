package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentService {
    Comments getCommentsAdv(Integer id);

    Comment addCommentAdv(Integer id, CreateOrUpdateComment createOrUpdateComment, String username);

    void deleteCommentAdv(Integer adId, Integer commentId, String username);

    Comment updateCommentAdv(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment, String username);
}
