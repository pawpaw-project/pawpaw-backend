package com.pawpaw.pawpaw.domain.post.controller;

import com.pawpaw.pawpaw.domain.post.dto.PostRequestDto;
import com.pawpaw.pawpaw.domain.post.dto.PostResponseDto;
import com.pawpaw.pawpaw.domain.post.service.PostService;
import com.pawpaw.pawpaw.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody PostRequestDto dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.createPost(dto, user));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(
            @RequestParam(required = false) String category) {
        if (category != null) {
            return ResponseEntity.ok(postService.getPostsByCategory(category));
        }
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto dto,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.updatePost(postId, dto, user));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user) {
        postService.deletePost(postId, user);
        return ResponseEntity.ok().build();
    }
}
