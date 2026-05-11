package com.pawpaw.pawpaw.domain.post.dto;

import com.pawpaw.pawpaw.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long id;
    private String category;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean likedByMe;

    public PostResponseDto(Post post) {
        this(post, false, post.getLikes().size());
    }

    public PostResponseDto(Post post, boolean likedByMe) {
        this(post, likedByMe, post.getLikes().size());
    }

    public PostResponseDto(Post post, boolean likedByMe, long likeCount) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.likeCount = (int) likeCount;
        this.likedByMe = likedByMe;
    }
}
