package com.openclassrooms.chatPOC.auth.services;

import com.openclassrooms.chatPOC.auth.payloads.LoginUserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public boolean loginUser(LoginUserRequest loginUserRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginUserRequest.getName(),
                            loginUserRequest.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e) {
            return false;
        }
        return true;
    }
}
