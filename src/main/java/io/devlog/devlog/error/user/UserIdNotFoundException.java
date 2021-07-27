package io.devlog.devlog.error.user;

public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(Long id) {
        super(String.format("User id not found: [%d]", id));
    }

}
