package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoOut {
    private Integer id;
    private String name;
    private String email;
    private String username;
    private String phoneNumber;
}
