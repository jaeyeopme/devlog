package io.devlog.devlog.post.controller;

import io.devlog.devlog.post.dto.PostResponse;
import io.devlog.devlog.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
