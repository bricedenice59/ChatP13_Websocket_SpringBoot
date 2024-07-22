package com.openclassrooms.chatPOC.chat.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSessionService {
    private final String sessionId = UUID.randomUUID().toString();
    private final Map<String, Set<String>> chatSession = new ConcurrentHashMap<>();

    public boolean addUserToSession(String username) {
        chatSession.putIfAbsent(sessionId, new HashSet<>());
        Set<String> users = chatSession.get(sessionId);

        //only 2 users can chat in a session
        if (users.size() < 2) {
            users.add(username);
            return true;
        }
        return false;
    }

    public void removeUserFromSession(String username) {
        Set<String> users = chatSession.get(sessionId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                chatSession.remove(sessionId);
            }
        }
    }
}

