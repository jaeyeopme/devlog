package io.devlog.devlog.user.domain.entity;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.common.jpa.BaseTimeEntity;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.user.dto.UserRegisterRequest;
import io.devlog.devlog.user.dto.UserUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Protected 수준의 기본 생성자까지 JPA  에서 찾을 수 있습니다.
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

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public User(String email, String password, String nickname) {
        this.authorities = new HashSet<>();
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static User from(UserRegisterRequest request, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void updateProfile(UserUpdateRequest request) {
        this.nickname = request.getNickname();
    }

    public void setEnable() {
        this.enabled = true;
    }

}
