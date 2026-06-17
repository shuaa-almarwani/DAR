package com.example.DAR.DTO.Out;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSummaryDTOOut {

    private Integer totalNotifications;
    private Integer unreadNotifications;
    private Integer readNotifications;
}