package org.dev.server.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseLogin {
    private String token;
    private  String message;
    private  UUID userId;
    private  String username;
}
