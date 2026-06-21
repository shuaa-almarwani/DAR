package com.example.DAR.DTO.Out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillComparisonResponseDtoOut {
    private String type;
    private List<BillComparisonDtoOut> data;
    private String aiNote;
}
