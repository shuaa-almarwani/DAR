package com.example.DAR.Security;

import com.example.DAR.Model.*;
import com.example.DAR.Repository.BillRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.MaintenanceRepository;
import com.example.DAR.Repository.MaintenanceReminderRepository;
import com.example.DAR.Repository.NotificationRepository;
import com.example.DAR.Repository.PurchaseInvoiceRepository;
import com.example.DAR.Repository.SensorReadingRepository;
import com.example.DAR.Repository.SensorRepository;
import com.example.DAR.Repository.UserRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final HomeRepository homeRepository;
    private final MaintenanceReminderRepository maintenanceReminderRepository;
    private final HomeItemRepository homeItemRepository;
    private final BillRepository billRepository;
    private final SensorRepository sensorRepository;
    private final SensorReadingRepository sensorReadingRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final NotificationRepository notificationRepository;

    // Checks if the logged-in user matches this user id.
    public boolean isCurrentUser(Integer userId) {
        User user = getCurrentUser();
        return user != null && user.getId().equals(userId);
    }

    // Checks if the subscription belongs to the logged-in user.
    public boolean isCurrentUserSubscription(Integer subscriptionId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }

        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionById(subscriptionId);
        return subscription != null && subscription.getUser().getId().equals(user.getId());
    }

    // Checks if the home belongs to the logged-in user.
    public boolean isCurrentUserHome(Integer homeId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }

        Home home = homeRepository.findHomeById(homeId);
        return home != null && home.getUser().getId().equals(user.getId());
    }

    // Checks if the reminder belongs to the logged-in user's home.
    public boolean isCurrentUserReminder(Integer reminderId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }

        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(reminderId);
        return reminder != null && reminder.getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the home item belongs to the logged-in user's home.
    public boolean isCurrentUserHomeItem(Integer itemId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        HomeItem item = homeItemRepository.findHomeItemById(itemId);
        return item != null && item.getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the bill belongs to the logged-in user's home.
    public boolean isCurrentUserBill(Integer billId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        Bill bill = billRepository.findBillById(billId);
        return bill != null && bill.getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the sensor belongs to the logged-in user's home.
    public boolean isCurrentUserSensor(Integer sensorId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        Sensor sensor = sensorRepository.findSensorById(sensorId);
        return sensor != null && sensor.getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the reading belongs to the logged-in user's sensor.
    public boolean isCurrentUserSensorReading(Integer readingId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        SensorReading reading = sensorReadingRepository.findSensorReadingById(readingId);
        return reading != null && reading.getSensors().getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the maintenance record belongs to the logged-in user's home.
    public boolean isCurrentUserMaintenance(Integer maintenanceId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        Maintenance maintenance = maintenanceRepository.findMaintenanceById(maintenanceId);
        return maintenance != null && maintenance.getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the invoice belongs to the logged-in user's home.
    public boolean isCurrentUserPurchaseInvoice(Integer invoiceId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        PurchaseInvoice invoice = purchaseInvoiceRepository.findPurchaseInvoiceById(invoiceId);
        return invoice != null && invoice.getHome().getUser().getId().equals(user.getId());
    }

    // Checks if the notification belongs to the logged-in user.
    public boolean isCurrentUserNotification(Integer notificationId) {
        User user = getCurrentUser();
        if (user == null) {
            return false;
        }
        Notification notification = notificationRepository.findNotificationById(notificationId);
        return notification != null && notification.getUser() != null && notification.getUser().getId().equals(user.getId());
    }

    // Gets the logged-in user from the security context.
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return null;
        }
        return userRepository.findUserByUsername(authentication.getName());
    }

    // Returns the logged-in user's id.
    public Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user == null ? null : user.getId();
    }
}
