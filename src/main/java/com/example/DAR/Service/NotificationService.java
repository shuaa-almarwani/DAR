package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.Out.NotificationSummaryDTOOut;
import com.example.DAR.Model.Notification;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.NotificationRepository;
import com.example.DAR.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public void sendWelcomeNotification(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        String message =
                "مرحبًا " + user.getName() + "، يسعدنا انضمامك إلى منصة دار. " +
                        "نتمنى لك تجربة سهلة وذكية في متابعة صيانة منزلك وتنظيم تنبيهاتك.";

        sendNotification(
                user,
                "WELCOME",
                "مرحبًا بك في دار",
                message
        );
    }

    public void sendProfileUpdatedNotification(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        String message =
                "مرحبًا " + user.getName() + "، تم تحديث بيانات ملفك الشخصي بنجاح في منصة دار.";

        sendNotification(
                user,
                "PROFILE_UPDATED",
                "تم تحديث الملف الشخصي",
                message
        );
    }

    public void sendAccountDeletedNotification(User user) {

        if (user == null) {
            throw new ApiException("User not found");
        }

        String message =
                "مرحبًا " + user.getName() + "، نود إبلاغك بأنه تم حذف حسابك من منصة دار. " +
                        "نشكر لك استخدامك للمنصة.";

        sendNotification(
                user,
                "ACCOUNT_DELETED",
                "تم حذف الحساب",
                message
        );
    }

    private void sendNotification(User user,
                                  String type,
                                  String title,
                                  String message) {

        String subject;

        switch (type) {
            case "WELCOME":
                subject = "مرحبًا بك في دار";
                break;

            case "PROFILE_UPDATED":
                subject = "تم تحديث ملفك الشخصي";
                break;

            case "ACCOUNT_DELETED":
                subject = "تم حذف حسابك من دار";
                break;

            default:
                subject = "إشعار من دار";
        }

        try {
            String htmlMessage =
                    buildEmailTemplate(subject, message.replace("\n", "<br>"));

            emailService.sendEmail(
                    user.getEmail(),
                    subject,
                    htmlMessage
            );

        } catch (Exception e) {
            System.out.println("Email not sent: " + e.getMessage());
        }

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
//        notification.setIsRead(false);
        notification.setSentAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    private String buildEmailTemplate(String title, String message) {

        return """
        <div dir="rtl" style="margin:0; padding:0; background-color:#E8DED2; font-family: Arial, sans-serif;">
            <div style="max-width:680px; margin:auto; padding:34px 18px;">
                
                <div style="
                    background: linear-gradient(135deg, #FFF8F4 0%%, #F7E8E1 55%%, #E8DED2 100%%);
                    border-radius:24px;
                    overflow:hidden;
                    border:1px solid #E2CFC3;
                    box-shadow:0 12px 32px rgba(118,83,69,0.14);
                ">
                    
                    <!-- Header -->
                    <div style="
                        padding:24px 28px 18px;
                        display:flex;
                        align-items:center;
                        justify-content:space-between;
                        border-bottom:1px solid rgba(166,137,114,0.22);
                    ">
                        <div style="text-align:right;">
                            <p style="
                                margin:0 0 8px;
                                display:inline-block;
                                background-color:#F3DCD2;
                                color:#765345;
                                padding:7px 14px;
                                border-radius:999px;
                                font-size:13px;
                            ">
                                إشعار من منصة دار
                            </p>
                            <h1 style="
                                margin:0;
                                color:#765345;
                                font-size:32px;
                                font-weight:800;
                                letter-spacing:-1px;
                            ">
                                دار
                            </h1>
                            <p style="
                                margin:6px 0 0;
                                color:#A68972;
                                font-size:15px;
                            ">
                                منصة ذكية لصيانة منزلك
                            </p>
                        </div>

                        <div style="
                            width:78px;
                            height:78px;
                            border-radius:20px;
                            background-color:#FFFFFF;
                            border:1px solid #E8DED2;
                            display:flex;
                            align-items:center;
                            justify-content:center;
                            box-shadow:0 8px 20px rgba(118,83,69,0.10);
                            font-size:38px;
                        ">
                            🏠
                        </div>
                    </div>

                    <!-- Body -->
                    <div style="padding:34px 30px 30px; text-align:right;">
                        <h2 style="
                            color:#765345;
                            font-size:24px;
                            margin:0 0 18px;
                            font-weight:800;
                        ">
                            %s
                        </h2>

                        <p style="
                            font-size:17px;
                            line-height:2;
                            color:#3E302A;
                            margin:0;
                        ">
                            %s
                        </p>

                        <div style="
                            margin-top:28px;
                            padding:18px 20px;
                            background-color:rgba(255,255,255,0.72);
                            border:1px solid rgba(166,137,114,0.24);
                            border-radius:18px;
                        ">
                            <p style="
                                margin:0;
                                color:#765345;
                                font-size:15px;
                                line-height:1.8;
                            ">
                                يمكنك متابعة تفاصيل منزلك، الصيانة، الفواتير، والتنبيهات من خلال منصة دار.
                            </p>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div style="
                        background-color:#3B241C;
                        padding:18px 28px;
                        text-align:center;
                    ">
                        <p style="
                            font-size:13px;
                            color:#E8DED2;
                            margin:0;
                        ">
                            © 2026 دار. جميع الحقوق محفوظة.
                        </p>
                        <p style="
                            font-size:12px;
                            color:#A68972;
                            margin:8px 0 0;
                        ">
                            هذه رسالة تلقائية من منصة دار
                        </p>
                    </div>
                </div>
            </div>
        </div>
        """.formatted(title, message);
    }

    public List<Notification> getNotificationsByUser(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        return notificationRepository.findNotificationsByUserId(userId);
    }

    public List<Notification> getUnreadNotificationsByUser(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        return notificationRepository.findNotificationsByUserIdAndIsRead(userId, false);
    }

    public void markNotificationAsRead(Integer notificationId) {

        Notification notification = notificationRepository.findNotificationById(notificationId);

        if (notification == null) {
            throw new ApiException("Notification not found");
        }

        notification.setIsRead(true);

        notificationRepository.save(notification);
    }

    public void markAllNotificationsAsRead(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        List<Notification> notifications = notificationRepository.findNotificationsByUserIdAndIsRead(userId, false);

        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }

        notificationRepository.saveAll(notifications);
    }

    public void deleteNotification(Integer notificationId) {

        Notification notification = notificationRepository.findNotificationById(notificationId);

        if (notification == null) {
            throw new ApiException("Notification not found");
        }

        notificationRepository.delete(notification);
    }

    public NotificationSummaryDTOOut getNotificationSummary(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        List<Notification> notifications = notificationRepository.findNotificationsByUserId(userId);

        int totalNotifications = notifications.size();

        int unreadNotifications = (int) notifications.stream()
                .filter(notification -> !notification.getIsRead())
                .count();

        int readNotifications = totalNotifications - unreadNotifications;

        return new NotificationSummaryDTOOut(
                totalNotifications,
                unreadNotifications,
                readNotifications
        );
    }
}

