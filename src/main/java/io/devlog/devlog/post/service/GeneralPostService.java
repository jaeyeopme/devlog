package io.devlog.devlog.post.service;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralPostService implements PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> POST_NOT_FOUND_EXCEPTION);
    }

    @Transactional
    @Override
    public void writPost(Post post) {
        postRepository.save(post);
    }

}
