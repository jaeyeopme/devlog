package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.dto.UserUpdateRequest;

public interface UserService {

    void register(User user);

    User findById(Long id);

    User findByEmail(String email);

    User updateProfile(String email, UserUpdateRequest request);

    void deleteProfileByEmail(String email);

    boolean isDuplicated(String email);

    void setEnable(String email);

}
