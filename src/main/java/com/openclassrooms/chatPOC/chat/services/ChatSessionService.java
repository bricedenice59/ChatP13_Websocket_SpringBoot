package com.openclassrooms.chatPOC.chat.services;

import com.openclassrooms.chatPOC.chat.ChatSessionDescriptor;
import com.openclassrooms.chatPOC.models.enums.RoleName;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSessionService {
    private static final Integer ROOM_SIZE = 2;
    private final String sessionId = UUID.randomUUID().toString();
    private final Map<String, Set<ChatSessionDescriptor>> chatSession = new ConcurrentHashMap<>();

    public boolean addUserToSession(String username, String role) {
        chatSession.putIfAbsent(sessionId, new HashSet<>());
        Set<ChatSessionDescriptor> usersInSession = chatSession.get(sessionId);

        // Check if the session already contains a user with the given role
        boolean hasUserRole = usersInSession.stream().anyMatch(user -> user.role.equals(RoleName.USER));
        boolean hasAdminRole = usersInSession.stream().anyMatch(user -> user.role.equals(RoleName.ADMIN));

        if ((role.equals(RoleName.USER.getDisplayName()) && hasUserRole) ||
                (role.equals(RoleName.ADMIN.getDisplayName()) && hasAdminRole)) {
            return false; // Role already exists in the session
        }

        //only 2 users can chat in a session
        if (usersInSession.size() < ROOM_SIZE) {
            usersInSession.add(ChatSessionDescriptor
                    .builder()
                    .username(username)
                    .role(RoleName.fromDisplayName(role))
                    .build());
            return true;
        }
        return false;
    }

    public void clearSession() {
        chatSession.clear();
    }
}

