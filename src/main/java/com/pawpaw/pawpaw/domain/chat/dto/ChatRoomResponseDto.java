package com.pawpaw.pawpaw.domain.chat.dto;

import com.pawpaw.pawpaw.domain.chat.entity.ChatRoom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatRoomResponseDto {

    private Long id;
    private String requesterNickname;
    private String receiverNickname;
    private LocalDateTime createdAt;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.requesterNickname = chatRoom.getRequester().getNickname();
        this.receiverNickname = chatRoom.getReceiver().getNickname();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
