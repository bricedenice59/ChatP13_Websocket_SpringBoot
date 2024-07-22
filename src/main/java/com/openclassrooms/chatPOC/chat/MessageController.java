package com.openclassrooms.chatPOC.chat;

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

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void sendToSpecificUser(@Payload ChatMessage message) {
        log.info("Forwarding chat message to user: {}", message.getReceiverName());
        messagingTemplate.convertAndSendToUser(message.getReceiverName(), "/topic", message);
    }

    @MessageMapping("/join")
    public void join(Principal principal){
        String joinMessage = principal.getName() + " has joined the chat";
        log.info(joinMessage);
        messagingTemplate.convertAndSend("/topic/join", joinMessage);
    }
}
