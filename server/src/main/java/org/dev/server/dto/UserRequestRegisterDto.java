package org.dev.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.dev.server.model.enums.KYCStatus;
import org.springframework.validation.annotation.Validated;

public record UserRequestRegisterDto(
        @NotBlank(message = "Username should not be blank.")
        @Size(min = 1, max = 32, message = "Username length should be between 1 and 32 characters long.")
        String username,

        @NotBlank(message = "Email should not be blank.")
        @Email(message = "Invalid format for email.")
        String email,

        @NotBlank(message = "Password should not be empty.")
        @Size(min = 4, max = 16, message = "Password length should be between 4 and 16 characters long.")
        String password
) {
}