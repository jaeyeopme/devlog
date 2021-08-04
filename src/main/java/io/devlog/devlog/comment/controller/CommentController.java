package io.devlog.devlog.comment.controller;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.comment.dto.CommentRequest;
import io.devlog.devlog.comment.dto.CommentResponse;
import io.devlog.devlog.comment.service.CommentService;
import io.devlog.devlog.error.common.CommentAccessDeniedException;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.post.service.PostService;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.devlog.devlog.comment.controller.CommentController.COMMENT_API_URI;

@RequiredArgsConstructor
@RequestMapping(COMMENT_API_URI)
@RestController
public class CommentController {

    public static final String COMMENT_API_URI = "/api/comments";
    private final PostService postService;
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void write(@Valid @RequestBody CommentRequest commentRequest,
                      @AuthenticationPrincipal PrincipalDetails userDetails) {
        Post post = postService.findById(commentRequest.getPostId());

        commentService.write(Comment.from(commentRequest, post, userDetails.getUser()));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public CommentResponse getComment(@PathVariable Long id) {
        return CommentResponse.from(commentService.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public CommentResponse modify(@PathVariable Long id,
                                  @Valid @RequestBody CommentRequest commentRequest,
                                  @AuthenticationPrincipal PrincipalDetails userDetails) {
        Comment comment = commentService.findById(id);

        if (comment.isNotAuthor(userDetails.getUser()))
            throw new CommentAccessDeniedException();

        commentService.modify(comment, commentRequest);

        return CommentResponse.from(comment);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id,
                                             @AuthenticationPrincipal PrincipalDetails userDetails) {
        Comment comment = commentService.findById(id);

        if (comment.isNotAuthor(userDetails.getUser()))
            throw new CommentAccessDeniedException();

        commentService.deleteById(id);
    }

}
