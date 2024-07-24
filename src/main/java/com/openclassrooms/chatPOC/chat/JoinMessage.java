package com.openclassrooms.chatPOC.chat;

import com.openclassrooms.chatPOC.chat.enums.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinMessage {
    private String senderName;
    private MessageType messageType;
}
