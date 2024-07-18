package com.openclassrooms.chatPOC.auth.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationResponse {
    private String token;
}