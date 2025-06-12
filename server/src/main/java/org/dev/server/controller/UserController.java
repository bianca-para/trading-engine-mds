package org.dev.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dev.server.dto.user.UserRequestLoginDto;
import org.dev.server.dto.user.UserRequestRegisterDto;
import org.dev.server.dto.user.UserResponseDto;
import org.dev.server.dto.user.UserResponseLogin;
import org.dev.server.jwtconfig.JWTUtil;
import org.dev.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


//test
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // load the full user so you can get its id
        UserResponseDto user = userService.findByUsername(request.getUsername());

        String token = util.generateToken(user.username(), user.id());

        // include id + username in the DTO if you want to echo them back
        return ResponseEntity.ok(
                new UserResponseLogin(
                        token,
                        "Token generated successfully!",
                        user.id(),
                        user.username()
                )
        );
    }


    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        UserResponseDto userResponseDto = userService.getUserById(id);
        return ResponseEntity.ok().body(userResponseDto);
    }

}
