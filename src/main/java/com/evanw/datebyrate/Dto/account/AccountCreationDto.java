package com.evanw.datebyrate.Dto.account;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record AccountCreationDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        String gender,
        @NotBlank
        String sexualOrientation,
        @NotBlank
        String dob
) {
}
