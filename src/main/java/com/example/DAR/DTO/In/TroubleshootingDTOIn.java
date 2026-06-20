package com.example.DAR.DTO.In;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TroubleshootingDTOIn {

    @NotEmpty(message = "Issue description is required")
    private String issueDescription;
}
