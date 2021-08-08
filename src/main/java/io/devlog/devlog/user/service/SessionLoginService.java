package io.devlog.devlog.user.service;

import io.devlog.devlog.error.user.UserEmailNotFoundException;
import io.devlog.devlog.user.domain.entity.PrincipalDetails;
import io.devlog.devlog.user.domain.entity.User;
import io.devlog.devlog.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SessionLoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserEmailNotFoundException(email));

        return PrincipalDetails.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .enabled(user.isEnabled())
                .build();
    }
}
