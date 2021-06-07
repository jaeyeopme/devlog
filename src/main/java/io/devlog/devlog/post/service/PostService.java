package io.devlog.devlog.post.service;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.dto.PostRequest;

public interface PostService {

    Post write(Post post);

    Post findById(Long id);

    void modify(Post post, PostRequest postRequest);

    void deleteById(Long id);

}
