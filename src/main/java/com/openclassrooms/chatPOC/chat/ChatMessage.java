package com.openclassrooms.chatPOC.chat;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String senderName;
    private MessageType messageType;
}
