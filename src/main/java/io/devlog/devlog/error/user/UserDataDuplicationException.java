package io.devlog.devlog.error.user;

public class UserDataDuplicationException extends RuntimeException {

    public UserDataDuplicationException() {
        super("User's some data is already existed");
    }

}
