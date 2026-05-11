package com.pawpaw.pawpaw.domain.post.service;

import com.pawpaw.pawpaw.domain.pet.entity.Pet;
import com.pawpaw.pawpaw.domain.pet.repository.PetRepository;
import com.pawpaw.pawpaw.domain.post.dto.PostRequestDto;
import com.pawpaw.pawpaw.domain.post.dto.PostResponseDto;
import com.pawpaw.pawpaw.domain.post.entity.Post;
import com.pawpaw.pawpaw.domain.post.repository.PostLikeRepository;
import com.pawpaw.pawpaw.domain.post.repository.PostRepository;
import com.pawpaw.pawpaw.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PetRepository petRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto dto, User user) {
        Pet pet = null;

        Post post = Post.builder()
                .user(user)
                .category(dto.getCategory())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();

        return new PostResponseDto(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts(User user) {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return toResponseDtos(posts, user);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByCategory(String category, User user) {
        List<Post> posts = postRepository.findByCategoryOrderByCreatedAtDesc(category);
        return toResponseDtos(posts, user);
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        boolean likedByMe = user != null
                && postLikeRepository.existsByPostIdAndUserId(postId, user.getId());
        return new PostResponseDto(post, likedByMe);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto dto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        post.update(dto.getTitle(), dto.getContent(), dto.getCategory());
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    private List<PostResponseDto> toResponseDtos(List<Post> posts, User user) {
        if (posts.isEmpty()) {
            return List.of();
        }
        Set<Long> likedPostIds = resolveLikedPostIds(posts, user);
        return posts.stream()
                .map(p -> new PostResponseDto(p, likedPostIds.contains(p.getId())))
                .collect(Collectors.toList());
    }

    private Set<Long> resolveLikedPostIds(List<Post> posts, User user) {
        if (user == null) {
            return Collections.emptySet();
        }
        List<Long> postIds = posts.stream().map(Post::getId).filter(Objects::nonNull).toList();
        if (postIds.isEmpty()) {
            return Collections.emptySet();
        }
        return postLikeRepository.findPostIdsByUserIdAndPostIdIn(user.getId(), postIds);
    }
}
