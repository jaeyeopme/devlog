package io.devlog.devlog.comment.controller;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.dto.CommentRequest;
import io.devlog.devlog.comment.dto.CommentResponse;
import io.devlog.devlog.comment.service.CommentService;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.comment.controller.CommentController.COMMENT_API_URI;
import static io.devlog.devlog.comment.exception.CommentResponseException.COMMENT_UNAUTHORIZED_EXCEPTION;
import static io.devlog.devlog.common.HttpStatusResponseEntity.RESPONSE_CREATED;
import static io.devlog.devlog.common.HttpStatusResponseEntity.RESPONSE_OK;

@RequiredArgsConstructor
@RequestMapping(COMMENT_API_URI)
@RestController
public class CommentController {

    public static final String COMMENT_API_URI = "/api/comments";
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<HttpStatus> write(@Valid @RequestBody CommentRequest commentRequest,
                                            @AuthenticationPrincipal PrincipalDetails userDetails) {
        Post post = postService.findById(commentRequest.getPostId());
        commentService.write(CommentRequest.toEntity(commentRequest, post, userDetails.getUser()));

        return RESPONSE_CREATED;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        return ResponseEntity.ok(CommentResponse.of(commentService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> modify(@PathVariable Long id,
                                                  @Valid @RequestBody CommentRequest commentRequest,
                                                  @AuthenticationPrincipal PrincipalDetails userDetails) {
        Comment comment = commentService.findById(id);

        if (comment.isNotAuthor(userDetails.getUser()))
            throw COMMENT_UNAUTHORIZED_EXCEPTION;

        commentService.modify(comment, commentRequest);

        return ResponseEntity.ok(CommentResponse.of(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id,
                                             @AuthenticationPrincipal PrincipalDetails userDetails) {
        Comment comment = commentService.findById(id);

        if (comment.isNotAuthor(userDetails.getUser()))
            throw COMMENT_UNAUTHORIZED_EXCEPTION;

        commentService.deleteById(id);

        return RESPONSE_OK;
    }

}
