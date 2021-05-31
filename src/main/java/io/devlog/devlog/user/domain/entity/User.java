package io.devlog.devlog.user.domain.entity;

import io.devlog.devlog.common.config.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Protected 수준의 기본 생성자까지 JPA 에서 찾을 수 있습니다.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(joinColumns = @JoinColumn(nullable = false))
    private Set<Authority> authorities;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean enabled;

    @Builder
    public User(String email, String password, String nickname) {
        this.authorities = new HashSet<>();
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    private void setEnabled() {
        this.enabled = true;
    }

    public static void setEnabled(User user) {
        user.setEnabled();
    }

}
