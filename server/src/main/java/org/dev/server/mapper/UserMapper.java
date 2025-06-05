package org.dev.server.mapper;

import org.dev.server.dto.user.UserRequestRegisterDto;
import org.dev.server.dto.user.UserResponseDto;
import org.dev.server.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserMapper {
    public static User toEntity(UserRequestRegisterDto userRequestRegisterDto, String hashedPassword) {

        Set<String> roles = new HashSet<>();
        roles.add("TRADER");

        return new User(
                userRequestRegisterDto.username(),
                userRequestRegisterDto.email(),
                hashedPassword,
                "PENDING",
                LocalDate.now(),
                roles
        );
    }
    public static UserResponseDto toDto(User user){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getKycStatus(),
                user.getRegisteredDate(),
                user.getRoles()
        );
    }
}