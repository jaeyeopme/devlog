package io.devlog.devlog.error.comment;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long id) {
        super(String.format("Comment id not found: [%d]", id));
    }

}
