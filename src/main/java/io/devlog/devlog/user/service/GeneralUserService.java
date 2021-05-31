package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.devlog.devlog.user.exception.UserResponseStatusException.NOT_FOUND_USER_EXCEPTION;

@RequiredArgsConstructor
@Service
public class GeneralUserService implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean isDuplicatedEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void register(User user) {
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void setEnabled(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> NOT_FOUND_USER_EXCEPTION);
        user.setEnabled(true);
    }

}
