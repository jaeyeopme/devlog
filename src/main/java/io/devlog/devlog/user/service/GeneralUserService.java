package io.devlog.devlog.user.service;

import io.devlog.devlog.error.user.UserEmailNotFoundException;
import io.devlog.devlog.error.user.UserIdNotFoundException;
import io.devlog.devlog.user.domain.entity.Authority;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new UserIdNotFoundException(id));
    }

    @Transactional
    @Override
    public User updateProfile(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        user.updateProfile(request);

        return user;
    }

    @Transactional
    @Override
    public void deleteProfileByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));
    }

    @Transactional
    @Override
    public void setEnable(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        user.setEnable();
    }

}
