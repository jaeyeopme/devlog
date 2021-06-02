package io.devlog.devlog.post.controller;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.post.dto.PostResponse;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.common.constant.ResponseEntityConstant.RESPONSE_CREATED;
import static io.devlog.devlog.common.constant.ResponseEntityConstant.RESPONSE_OK;
import static io.devlog.devlog.post.controller.PostController.POST_API_URI;
import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_UNAUTHORIZED_EXCEPTION;

@RequiredArgsConstructor
@RequestMapping(POST_API_URI)
@RestController
public class PostController {

    public static final String POST_API_URI = "/api/posts";
    private final PostService postService;

    @PostMapping
    public ResponseEntity<HttpStatus> write(@Valid @RequestBody PostRequest postRequest,
                                            @AuthenticationPrincipal User author) {
        postService.write(PostRequest.toEntity(postRequest, author));

        return RESPONSE_CREATED;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(PostResponse.of(postService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> modify(@PathVariable Long id,
                                               @Valid @RequestBody PostRequest postRequest,
                                               @AuthenticationPrincipal User author) {
        Post post = postService.findById(id);

        if (post.isNotAuthor(author))
            throw POST_UNAUTHORIZED_EXCEPTION;

        postService.modify(post, postRequest);

        return ResponseEntity.ok(PostResponse.of(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id,
                                             @AuthenticationPrincipal User author) {
        Post post = postService.findById(id);

        if (post.isNotAuthor(author))
            throw POST_UNAUTHORIZED_EXCEPTION;

        postService.deleteById(id);

        return RESPONSE_OK;
    }

}
