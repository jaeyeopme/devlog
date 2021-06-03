package io.devlog.devlog.comment.service;

import io.devlog.devlog.comment.domain.entity.Comment;

public interface CommentService {

    void write(Comment comment);

    Comment findById(Long id);

}
