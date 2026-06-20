package com.example.DAR.DTO.In;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChatbotDTOIn {

    @NotEmpty(message = "Question must not be empty")
    private String question;
}