package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserUpdateRequest;

public interface UserService {

    void register(User user);

    User findById(Long id);

    User findByEmail(String email);

    void updateProfile(User user, UserUpdateRequest userUpdateRequest);

    void deleteProfile(Long id);

    boolean isDuplicated(String email);

    void setEnabled(User user);

}
