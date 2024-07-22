package com.openclassrooms.chatPOC.chat;

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
