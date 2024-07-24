package com.openclassrooms.chatPOC.chat;

import com.openclassrooms.chatPOC.models.enums.RoleName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChatSessionDescriptor {
    public String username;
    public RoleName role;
}
