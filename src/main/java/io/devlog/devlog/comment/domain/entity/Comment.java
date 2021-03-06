package io.devlog.devlog.comment.domain.entity;

import io.devlog.devlog.comment.dto.CommentRequest;
import io.devlog.devlog.common.jpa.BaseTimeEntity;
import io.devlog.devlog.post.domain.entity.Post;
import io.devlog.devlog.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User author;

    @Builder
    public Comment(String content, Post post, User author) {
        this.content = content;
        this.post = post;
        this.author = author;
    }

    public static Comment from(CommentRequest commentRequest, Post post, User author) {
        return Comment.builder()
                .content(commentRequest.getContent())
                .post(post)
                .author(author)
                .build();
    }

    public void modify(CommentRequest commentRequest) {
        this.content = commentRequest.getContent();
    }

    public boolean isNotAuthor(User author) {
        return !this.author.equals(author);
    }

}
