package io.devlog.devlog.user.service;

import io.devlog.devlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.devlog.devlog.user.exception.UserResponseStatusException.USER_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
public class SessionLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> USER_NOT_FOUND_EXCEPTION);
    }

}
