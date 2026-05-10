package com.pawpaw.pawpaw.domain.post.service;

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
    public boolean toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (postLikeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            postLikeRepository.deleteByPostIdAndUserId(postId, user.getId());
            return false;
        } else {
            postLikeRepository.save(PostLike.builder()
                    .post(post)
                    .user(user)
                    .build());
            return true;
        }
    }
}
