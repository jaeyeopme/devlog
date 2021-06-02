package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserRequest;

public interface UserService {

    void register(User user);

    User findByEmail(String email);

    void updateUserProfile(User user, UserRequest userRequest);

    void delete(Long id);

    boolean isDuplicated(String email);

    void setEnabled(User user);

}
