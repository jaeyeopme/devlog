package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.User;

import java.util.Optional;

public interface UserService {

    boolean isDuplicatedEmail(String email);

    void register(User user);

    Optional<User> findByEmail(String email);

    void setEnabledByEmail(String email);

}
