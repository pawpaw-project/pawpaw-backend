package com.pawpaw.pawpaw.domain.chat.repository;

import com.pawpaw.pawpaw.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByRequesterIdOrReceiverId(Long requesterId, Long receiverId);
    Optional<ChatRoom> findByRequesterIdAndReceiverIdAndWalkRequestId(Long requesterId, Long receiverId, Long walkRequestId);
}
