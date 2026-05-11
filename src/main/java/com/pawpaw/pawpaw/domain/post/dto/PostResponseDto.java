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
        this(post, false);
    }

    public PostResponseDto(Post post, boolean likedByMe) {
        this.id = post.getId();
        this.category = post.getCategory();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.likeCount = post.getLikes().size();
        this.likedByMe = likedByMe;
    }
}
