package com.openclassrooms.chatPOC.auth.services;

import com.openclassrooms.chatPOC.auth.payloads.LoginUserRequest;
 import com.openclassrooms.chatPOC.auth.responses.AuthenticationResponse;
import com.openclassrooms.chatPOC.models.User;
import com.openclassrooms.chatPOC.security.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse loginUser(LoginUserRequest loginUserRequest) {
        var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserRequest.getName(),
                            loginUserRequest.getPassword()
                    )
            );
        return getToken((User)authentication.getPrincipal());
    }

    private AuthenticationResponse getToken(User user) {
        var claims = new HashMap<String, Object>();

        claims.put("fullName", user.getUsername());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
