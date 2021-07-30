package io.devlog.devlog.post.service;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.domain.repository.PostRepository;
import io.devlog.devlog.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.common.redis.cache.CacheRedisConfig.POST;
import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralPostService implements PostService {

    private final PostRepository postRepository;

    @CachePut(cacheNames = POST, key = "#post.id")
    @Transactional
    @Override
    public Post write(Post post) {
        return postRepository.save(post);
    }

    @Cacheable(cacheNames = POST, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> POST_NOT_FOUND_EXCEPTION);
    }

    @CachePut(cacheNames = POST, key = "#post.id")
    @Transactional
    @Override
    public void modify(Post post, PostRequest postRequest) {
        post.modify(postRequest);
    }

    @CacheEvict(cacheNames = POST, key = "#id")
    @Transactional
    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

}
