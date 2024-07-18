package com.openclassrooms.chatPOC.auth.controllers;


import com.openclassrooms.chatPOC.auth.payloads.LoginUserRequest;
import com.openclassrooms.chatPOC.auth.responses.AuthenticationResponse;
import com.openclassrooms.chatPOC.auth.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Post - Login a user
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        var authenticationResponse = authenticationService.loginUser(loginUserRequest);
        return ResponseEntity.ok(authenticationResponse);
    }
}
