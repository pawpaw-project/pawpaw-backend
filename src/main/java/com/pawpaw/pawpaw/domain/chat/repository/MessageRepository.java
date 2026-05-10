package com.pawpaw.pawpaw.domain.chat.repository;

import com.pawpaw.pawpaw.domain.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long roomId);
}
