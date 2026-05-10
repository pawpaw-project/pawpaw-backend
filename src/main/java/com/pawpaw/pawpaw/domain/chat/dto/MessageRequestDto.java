package com.pawpaw.pawpaw.domain.chat.dto;

import lombok.Getter;

@Getter
public class MessageRequestDto {
    private Long roomId;
    private String content;
}
