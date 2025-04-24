package org.dev.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev.server.dto.UserRequestLoginDto;
import org.dev.server.dto.UserRequestRegisterDto;
import org.dev.server.dto.UserResponseDto;
import org.dev.server.dto.UserResponseLogin;
import org.dev.server.jwtconfig.JWTUtil;
import org.dev.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {



    private final UserService userService;
    private final JWTUtil util;
    private final AuthenticationManager authenticationManager;

    //adaug user
    //!!ruta de POST e lasata goala (""), ca se subintelege, nu trb o ruta noua gen /newUser
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestRegisterDto userRequestRegisterDto) {
        UserResponseDto userResponseDto = userService.createUser(userRequestRegisterDto);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseLogin> login(@RequestBody UserRequestLoginDto request){
        //Validate username/password with DB(required in case of Stateless Authentication)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));
        String token =util.generateToken(request.getUsername());
        return ResponseEntity.ok(new UserResponseLogin(token, "Token generated successfully!"));
    }


    //TO DO - DELETE dupa id(DELETE), UPDATE - POST SAU PATCH, GETALLUSERS - GET
}
