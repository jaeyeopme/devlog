package io.devlog.devlog.error.user;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        super(String.format("User email not found: [%s]", email));
    }

}
