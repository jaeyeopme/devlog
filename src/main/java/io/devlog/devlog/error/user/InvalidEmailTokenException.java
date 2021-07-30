package io.devlog.devlog.error.user;

public class InvalidEmailTokenException extends RuntimeException {

    public InvalidEmailTokenException(String emailToken) {
        super("Invalid email token: " + emailToken);
    }

}
