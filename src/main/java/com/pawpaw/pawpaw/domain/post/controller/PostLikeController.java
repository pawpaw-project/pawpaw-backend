package com.pawpaw.pawpaw.domain.post.controller;

import com.pawpaw.pawpaw.domain.post.service.PostLikeService;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}/likes")
    public ResponseEntity<String> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        boolean liked = postLikeService.toggleLike(postId, user);
        return ResponseEntity.ok(liked ? "좋아요" : "좋아요 취소");
    }
}
