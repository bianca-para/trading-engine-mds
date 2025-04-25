package org.dev.server.service;

import org.dev.server.dto.UserRequestRegisterDto;
import org.dev.server.dto.UserResponseDto;
import org.dev.server.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(UserRequestRegisterDto userRequestRegisterDto);

//    Optional<User> findByUsername(String username);
}
