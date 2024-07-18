package com.openclassrooms.chatPOC.config.Websockets;

import com.openclassrooms.chatPOC.chat.ChatMessage;
import com.openclassrooms.chatPOC.chat.MessageType;
import com.openclassrooms.chatPOC.chat.services.ChatSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatSessionService chatSessionService;

    @EventListener
    public void onWebSocketConnect(SessionConnectEvent event) {
        log.info("Establishing WebSocket connection...");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
//        String username = (String) accessor.getSessionAttributes().get("username");
//
//        var hasUserBeenAdded = chatSessionService.addUserToSession(sessionId, username);
//        var chatMessage = ChatMessage.builder()
//                .messageType(hasUserBeenAdded ? MessageType.JOIN : MessageType.FULL)
//                .senderName(username)
//                .build();
//        messageTemplate.convertAndSend("/specific", chatMessage);
//        messageTemplate.convertAndSendToUser(username,"/specific", chatMessage);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String username = (String) accessor.getSessionAttributes().get("username");

        if(username != null) {
            log.info("Received web socket disconnect event:{}", username);
            chatSessionService.removeUserFromSession(sessionId, username);
            var chatMessage = ChatMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .senderName(username)
                    .build();
            messageTemplate.convertAndSendToUser(username,"/specific", chatMessage);
        }
    }
}
