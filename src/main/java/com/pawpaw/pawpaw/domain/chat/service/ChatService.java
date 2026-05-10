package com.pawpaw.pawpaw.domain.chat.service;

import com.pawpaw.pawpaw.domain.chat.dto.ChatRoomResponseDto;
import com.pawpaw.pawpaw.domain.chat.dto.MessageRequestDto;
import com.pawpaw.pawpaw.domain.chat.dto.MessageResponseDto;
import com.pawpaw.pawpaw.domain.chat.entity.ChatRoom;
import com.pawpaw.pawpaw.domain.chat.entity.Message;
import com.pawpaw.pawpaw.domain.chat.repository.ChatRoomRepository;
import com.pawpaw.pawpaw.domain.chat.repository.MessageRepository;
import com.pawpaw.pawpaw.domain.user.entity.User;
import com.pawpaw.pawpaw.domain.user.repository.UserRepository;
import com.pawpaw.pawpaw.domain.walk.entity.WalkRequest;
import com.pawpaw.pawpaw.domain.walk.repository.WalkRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final WalkRequestRepository walkRequestRepository;

    @Transactional
    public ChatRoomResponseDto createChatRoom(Long walkRequestId, User requester, Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        WalkRequest walkRequest = walkRequestRepository.findById(walkRequestId)
                .orElseThrow(() -> new IllegalArgumentException("산책 요청을 찾을 수 없습니다."));

        chatRoomRepository.findByRequesterIdAndReceiverIdAndWalkRequestId(
                        requester.getId(), receiverId, walkRequestId)
                .ifPresent(r -> { throw new IllegalArgumentException("이미 존재하는 채팅방입니다."); });

        ChatRoom chatRoom = ChatRoom.builder()
                .requester(requester)
                .receiver(receiver)
                .walkRequest(walkRequest)
                .build();

        return new ChatRoomResponseDto(chatRoomRepository.save(chatRoom));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getMyChatRooms(User user) {
        return chatRoomRepository.findByRequesterIdOrReceiverId(user.getId(), user.getId())
                .stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDto> getMessages(Long roomId) {
        return messageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId)
                .stream()
                .map(MessageResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto dto, User sender) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(dto.getContent())
                .isRead(false)
                .build();

        return new MessageResponseDto(messageRepository.save(message));
    }
}
