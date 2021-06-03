package io.devlog.devlog.comment.service;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.dto.CommentRequest;

public interface CommentService {

    void write(Comment comment);

    Comment findById(Long id);

    void modify(Comment comment, CommentRequest commentRequest);

    void deleteById(Long id);

}
