package io.devlog.devlog.comment.service;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.domain.repository.CommentRepository;
import io.devlog.devlog.comment.dto.CommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.comment.exception.CommentResponseException.COMMENT_NOT_FOUND_EXCEPTION;
import static io.devlog.devlog.common.redis.cache.CacheRedisConfig.COMMENT;

@RequiredArgsConstructor
@Service
public class GeneralCommentService implements CommentService {

    private final CommentRepository commentRepository;

    @CachePut(cacheNames = COMMENT, key = "#comment.id")
    @Transactional
    @Override
    public Comment write(Comment comment) {
        return commentRepository.save(comment);
    }

    @Cacheable(cacheNames = COMMENT, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> COMMENT_NOT_FOUND_EXCEPTION);
    }

    @CachePut(cacheNames = COMMENT, key = "#comment.id")
    @Transactional
    @Override
    public void modify(Comment comment, CommentRequest commentRequest) {
        comment.modify(commentRequest);
    }

    @CacheEvict(cacheNames = COMMENT, key = "#id")
    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

}
