package io.devlog.devlog.post.domain.entity;

import io.devlog.devlog.comment.domain.entity.Comment;
import io.devlog.devlog.config.jpa.BaseTimeEntity;
import io.devlog.devlog.post.dto.PostRequest;
import io.devlog.devlog.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Builder
    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public static Post from(PostRequest postRequest, User user) {
        return Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getTitle())
                .author(user)
                .build();
    }

    public void modify(PostRequest postRequest) {
        this.title = postRequest.getTitle();
        this.content = postRequest.getContent();
    }

    public boolean isNotAuthor(User user) {
        return !this.author.equals(user);
    }

}
