package com.pawpaw.pawpaw.domain.chat.controller;

import com.pawpaw.pawpaw.domain.chat.dto.ChatRoomResponseDto;
import com.pawpaw.pawpaw.domain.chat.dto.MessageRequestDto;
import com.pawpaw.pawpaw.domain.chat.dto.MessageResponseDto;
import com.pawpaw.pawpaw.domain.chat.service.ChatService;
import com.pawpaw.pawpaw.domain.user.entity.User;
import com.pawpaw.pawpaw.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @PostMapping("/api/chat/rooms")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
            @RequestParam Long walkRequestId,
            @RequestParam Long receiverId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatService.createChatRoom(walkRequestId, user, receiverId));
    }

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getMyChatRooms(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatService.getMyChatRooms(user));
    }

    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageResponseDto>> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    @MessageMapping("/chat/message")
    public void sendMessage(MessageRequestDto dto, Principal principal) {
        User sender = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        MessageResponseDto message = chatService.saveMessage(dto, sender);
        messagingTemplate.convertAndSend("/sub/chat/room/" + dto.getRoomId(), message);
    }
}
