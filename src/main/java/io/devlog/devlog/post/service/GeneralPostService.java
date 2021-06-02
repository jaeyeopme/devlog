package io.devlog.devlog.post.service;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.domain.repository.PostRepository;
import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralPostService implements PostService {

    private final PostRepository postRepository;


    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> POST_NOT_FOUND_EXCEPTION);
    }

}
