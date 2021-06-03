package io.devlog.devlog.comment.service;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.domain.repository.CommentRepository;
import io.devlog.devlog.comment.dto.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.comment.exception.CommentResponseException.COMMENT_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralCommentService implements CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void write(Comment comment) {
        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);
    }

    @Transactional
    @Override
    public void modify(Comment comment, CommentRequest commentRequest) {
        comment.modify(commentRequest);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

}
