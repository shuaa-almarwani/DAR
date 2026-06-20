
package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.Model.Notification;
import com.example.DAR.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/notification")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get")
    public ResponseEntity<?> getNotification() {
        return ResponseEntity.status(200).body(notificationService.getAllNotifications());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUser(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(notificationService.getNotificationsByUser(userId));
    }

    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> getUnreadNotificationsByUser(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(notificationService.getUnreadNotificationsByUser(userId));
    }

    @PutMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Integer notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.status(200).body(new ApiResponse("Notification marked as read successfully"));
    }

    @PutMapping("/mark-all-as-read/{userId}")
    public ResponseEntity<?> markAllNotificationsAsRead(@PathVariable Integer userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.status(200).body(new ApiResponse("All notifications marked as read successfully"));
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(200).body(new ApiResponse("Notification deleted successfully"));
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<?> getNotificationSummary(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(notificationService.getNotificationSummary(userId));
    }

    @PostMapping("/weather-alert/{homeId}")
    public ResponseEntity<?> createWeatherAlertNotification(@PathVariable Integer homeId) {
        notificationService.createWeatherAlertNotification(homeId);
        return ResponseEntity.status(200).body(new ApiResponse("Weather alert notification sent successfully"));
    }

    @PostMapping("/smart-alert-intro/{userId}")
    public ResponseEntity<?> sendSmartAlertIntro(@PathVariable Integer userId) {
        notificationService.sendSmartAlertIntro(userId);
        return ResponseEntity.status(200).body(new ApiResponse("Smart alert introduction sent successfully"));
    }
}