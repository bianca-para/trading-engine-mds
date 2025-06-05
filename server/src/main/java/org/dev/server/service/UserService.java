package org.dev.server.service;

import org.dev.server.dto.user.UserRequestRegisterDto;
import org.dev.server.dto.user.UserResponseDto;

import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRequestRegisterDto userRequestRegisterDto);

    UserResponseDto getUserById(UUID id);

//    Optional<User> findByUsername(String username);
}
