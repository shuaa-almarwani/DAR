package com.example.DAR.DTO.In;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoIn {

    @NotEmpty(message = "name is required")
    @Size(min = 3, max = 20, message = "name must be between 3 and 20 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotEmpty(message = "Username is required")
    @Size(min = 3, max = 20, message = "username must be between 3 and 20 characters")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Password Confirmation is required")
    private String passwordConfirmation;

    @NotNull(message = "AcceptTermsAndConditions are required")
    private Boolean acceptTermsAndConditions;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^(?:\\+?966|0)?5[0-9]{8}$", message = "Invalid Saudi mobile number format")
    private String phoneNumber;

}
