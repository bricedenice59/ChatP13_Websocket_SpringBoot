package com.openclassrooms.chatPOC.chat;

import com.openclassrooms.chatPOC.chat.services.ChatSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatSessionService chatSessionService;

    public MessageController(SimpMessagingTemplate messagingTemplate, ChatSessionService chatSessionService) {
        this.messagingTemplate = messagingTemplate;
        this.chatSessionService = chatSessionService;
    }

    @MessageMapping("/chat")
    public void sendToSpecificUser(@Payload ChatMessage message) {
        log.info("Forwarding chat message to user: {}", message.getReceiverName());
        messagingTemplate.convertAndSendToUser(message.getReceiverName(), "/topic", message);
    }

    @MessageMapping("/join")
    public void join(@Payload JoinMessage message, Principal principal){
        var username = principal.getName();
        if(chatSessionService.addUserToSession(username)) {
            message.setSenderName(username);
            message.setMessageType(MessageType.JOIN);

            String joinMessage = username + " has joined the chat";
            log.info(joinMessage);
            messagingTemplate.convertAndSend("/topic/join", message);
            return;
        }
        log.info("Room is full! only 2 persons allowed");
    }
}
