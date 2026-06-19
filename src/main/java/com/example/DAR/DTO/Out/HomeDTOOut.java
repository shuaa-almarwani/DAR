package com.example.DAR.DTO.Out;

import com.example.DAR.Enums.HomePropertyType;
import lombok.Data;

@Data
public class HomeDTOOut {

    private Integer id;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Integer buildYear;
    private HomePropertyType propertyType;
    private String ownerName;  // from User.fullName
}
