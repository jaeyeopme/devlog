package io.devlog.devlog.post.domain.repository;

import io.devlog.devlog.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
