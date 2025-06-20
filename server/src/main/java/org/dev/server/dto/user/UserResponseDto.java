package org.dev.server.dto.user;

import org.dev.server.model.enums.KYCStatus;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        KYCStatus kycStatus,
        LocalDate registeredDate,
        Set<String> roles
) {
}
