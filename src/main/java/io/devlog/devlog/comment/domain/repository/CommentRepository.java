package io.devlog.devlog.comment.domain.repository;

import io.devlog.devlog.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
