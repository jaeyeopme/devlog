package io.devlog.devlog.comment.controller;

import io.devlog.devlog.comment.dto.CommentRequest;
import io.devlog.devlog.comment.service.CommentService;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static io.devlog.devlog.comment.controller.CommentController.COMMENT_API_URI;
import static io.devlog.devlog.common.constant.ResponseEntityConstant.RESPONSE_CREATED;

@RequiredArgsConstructor
@RequestMapping(COMMENT_API_URI)
@RestController
public class CommentController {

    public static final String COMMENT_API_URI = "/api/comments";
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<HttpStatus> write(@Valid @RequestBody CommentRequest commentRequest,
                                            @AuthenticationPrincipal User author) {
        Post post = postService.findById(commentRequest.getPostId());
        commentService.write(CommentRequest.toEntity(commentRequest, post, author));

        return RESPONSE_CREATED;
    }

}
