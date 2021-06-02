package io.devlog.devlog.post.service;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.user.domain.entity.User;

public interface PostService {

    Post findPost(Long id);

    void writPost(Post post);

}
