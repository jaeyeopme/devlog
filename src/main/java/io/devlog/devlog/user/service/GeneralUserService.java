package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.Authority;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import io.devlog.devlog.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.user.exception.UserResponseStatusException.USER_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void register(User user) {
        user.getAuthorities().add(Authority.USER);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> USER_NOT_FOUND_EXCEPTION);
    }

    @Transactional
    @Override
    public void updateUserProfile(User user, UserRequest userRequest) {
        user.updateProfile(userRequest);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isDuplicated(java.lang.String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional(readOnly = true)
    @Override
    public User findByEmail(java.lang.String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> USER_NOT_FOUND_EXCEPTION);
    }

    @Transactional
    @Override
    public void setEnabled(User user) {
        user.setEnabled();
    }

}
