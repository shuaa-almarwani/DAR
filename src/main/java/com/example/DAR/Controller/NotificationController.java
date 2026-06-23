
package com.example.DAR.Controller;

import com.example.DAR.Api.ApiResponse;
import com.example.DAR.Model.Notification;
import com.example.DAR.Security.SecurityService;
import com.example.DAR.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/notification")
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SecurityService securityService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getNotification() {
        return ResponseEntity.status(200).body(notificationService.getAllNotifications());
    }

    @GetMapping("/get/{notificationId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserNotification(#notificationId)")
    public ResponseEntity<?> getNotificationById(@PathVariable Integer notificationId) {
        return ResponseEntity.status(200).body(notificationService.getNotificationById(notificationId));
    }
    @GetMapping("/user-notifications")
    public ResponseEntity<?> getNotificationsByUser() {
        return ResponseEntity.status(200).body(notificationService.getNotificationsByUser(securityService.getCurrentUserId()));
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadNotificationsByUser() {
        return ResponseEntity.status(200).body(notificationService.getUnreadNotificationsByUser(securityService.getCurrentUserId()));
    }

    @PutMapping("/mark-as-read/{notificationId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserNotification(#notificationId)")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Integer notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.status(200).body(new ApiResponse("Notification marked as read successfully"));
    }

    @PutMapping("/mark-all-as-read")
    public ResponseEntity<?> markAllNotificationsAsRead() {
        notificationService.markAllNotificationsAsRead(securityService.getCurrentUserId());
        return ResponseEntity.status(200).body(new ApiResponse("All notifications marked as read successfully"));
    }

    @DeleteMapping("/delete/{notificationId}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUserNotification(#notificationId)")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(200).body(new ApiResponse("Notification deleted successfully"));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getNotificationSummary() {
        return ResponseEntity.status(200).body(notificationService.getNotificationSummary(securityService.getCurrentUserId()));
    }

    @PostMapping("/weather-alert/{homeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createWeatherAlertNotification(@PathVariable Integer homeId) {
        notificationService.createWeatherAlertNotification(homeId);
        return ResponseEntity.status(200).body(new ApiResponse("Weather alert notification sent successfully"));
    }

    @PostMapping("/smart-alert-intro/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sendSmartAlertIntro(@PathVariable Integer userId) {
        notificationService.sendSmartAlertIntro(userId);
        return ResponseEntity.status(200).body(new ApiResponse("Smart alert introduction sent successfully"));
    }
}
