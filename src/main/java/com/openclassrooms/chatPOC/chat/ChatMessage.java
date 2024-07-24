package com.openclassrooms.chatPOC.chat;

import com.openclassrooms.chatPOC.chat.enums.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String senderName;
    private String receiverName;
    private MessageType messageType;
}
