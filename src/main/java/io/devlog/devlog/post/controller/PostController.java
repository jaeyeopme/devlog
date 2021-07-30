package io.devlog.devlog.post.controller;

import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.post.dto.PostResponse;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.post.controller.PostController.POST_API_URI;
import static io.devlog.devlog.post.exception.PostResponseStatusException.POST_UNAUTHORIZED_EXCEPTION;

@RequiredArgsConstructor
@RequestMapping(POST_API_URI)
@RestController
public class PostController {

    public static final String POST_API_URI = "/api/posts";
    private final PostService postService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public void write(@Valid @RequestBody PostRequest postRequest,
                      @AuthenticationPrincipal PrincipalDetails details) {
        postService.write(Post.from(postRequest, details.getUser()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return PostResponse.of(postService.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public PostResponse modify(@PathVariable Long id,
                               @Valid @RequestBody PostRequest postRequest,
                               @AuthenticationPrincipal PrincipalDetails userDetails) {
        Post post = postService.findById(id);

        if (post.isNotAuthor(userDetails.getUser()))
            throw POST_UNAUTHORIZED_EXCEPTION;

        postService.modify(post, postRequest);

        return PostResponse.of(post);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                       @AuthenticationPrincipal PrincipalDetails userDetails) {
        Post post = postService.findById(id);

        if (post.isNotAuthor(userDetails.getUser()))
            throw POST_UNAUTHORIZED_EXCEPTION;

        postService.deleteById(id);
    }

}
