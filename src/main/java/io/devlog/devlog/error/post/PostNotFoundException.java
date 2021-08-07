package io.devlog.devlog.error.post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super(String.format("Post id not found: [%d]", id));
    }

}
