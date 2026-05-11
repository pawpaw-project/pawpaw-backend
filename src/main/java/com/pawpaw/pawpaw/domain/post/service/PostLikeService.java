package com.pawpaw.pawpaw.domain.post.service;

import com.pawpaw.pawpaw.domain.post.dto.PostResponseDto;
import com.pawpaw.pawpaw.domain.post.entity.Post;
import com.pawpaw.pawpaw.domain.post.entity.PostLike;
import com.pawpaw.pawpaw.domain.post.repository.PostLikeRepository;
import com.pawpaw.pawpaw.domain.post.repository.PostRepository;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        boolean likedByMe;
        if (postLikeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            postLikeRepository.deleteByPostIdAndUserId(postId, user.getId());
            likedByMe = false;
        } else {
            postLikeRepository.save(PostLike.builder()
                    .post(post)
                    .user(user)
                    .build());
            likedByMe = true;
        }

        return new PostResponseDto(post, likedByMe);
    }
}
