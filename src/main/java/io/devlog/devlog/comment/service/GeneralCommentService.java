package io.devlog.devlog.comment.service;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GeneralCommentService implements CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void write(Comment comment) {
        commentRepository.save(comment);
    }

}
