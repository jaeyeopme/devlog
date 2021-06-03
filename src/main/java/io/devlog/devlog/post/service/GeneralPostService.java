package io.devlog.devlog.post.service;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.domain.repository.PostRepository;
import io.devlog.devlog.post.dto.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralPostService implements PostService {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public void write(Post post) {
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    @Override
    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> POST_NOT_FOUND_EXCEPTION);
    }

    @Transactional
    @Override
    public void modify(Post post, PostRequest postRequest) {
        post.modify(postRequest);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

}
