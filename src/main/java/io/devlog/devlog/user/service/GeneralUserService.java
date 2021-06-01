package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.entity.Authority;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        user.getAuthorities().add(Authority.USER);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void setEnabledByEmail(String email) {
        userRepository.setEnabledByEmail(email);
    }

}
