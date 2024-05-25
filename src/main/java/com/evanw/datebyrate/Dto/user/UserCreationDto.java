package com.evanw.datebyrate.Dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreationDto(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String username,
        @NotBlank
        String password

) {

}
