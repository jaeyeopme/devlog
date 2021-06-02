package io.devlog.devlog.post.controller;

import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.post.dto.PostResponse;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import static io.devlog.devlog.common.constant.ResponseEntityConstant.RESPONSE_CREATED;
import static io.devlog.devlog.post.controller.PostController.POST_API_URI;

@RequestMapping(POST_API_URI)
@RequiredArgsConstructor
@RestController
public class PostController {

    public static final String POST_API_URI = "/api/posts";
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(PostResponse.of(postService.findPost(id)));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> writePost(@Valid @RequestBody PostRequest postRequest,
                                                @AuthenticationPrincipal User author) {
        postService.writPost(PostRequest.toEntity(postRequest, author));

        return RESPONSE_CREATED;
    }


}
