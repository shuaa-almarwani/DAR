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

    public void sendWarrantyExpiryNotification(com.example.DAR.Model.User user, String productName, String message) {
        if (user == null) throw new ApiException("User not found");
        sendNotification(user, "WARRANTY_EXPIRY", "تنبيه: ضمان \"" + productName + "\" على وشك الانتهاء", message);
    }

    public void sendBillDueNotification(com.example.DAR.Model.User user, String typeAr, String message) {
        if (user == null) throw new ApiException("User not found");
        sendNotification(user, "BILL_DUE", "تذكير: فاتورة " + typeAr + " قادمة", message);
    }

    public void sendBillAnomalyNotification(User user, String billType, int currentConsumption, double avgConsumption, String aiExplanation) {
        if (user == null) throw new ApiException("User not found");

        String typeAr = switch (billType.toUpperCase()) {
            case "ELECTRICITY" -> "الكهرباء";
            case "WATER" -> "الماء";
            case "GAS" -> "الغاز";
            default -> billType;
        };

        int percentage = (int) Math.round(((currentConsumption - avgConsumption) / avgConsumption) * 100);

        String message = "مرحبًا " + user.getName() + "،\n" +
                "لاحظنا ارتفاعًا غير طبيعي في فاتورة " + typeAr + " بنسبة " + percentage + "%.\n\n" +
                aiExplanation;

        sendNotification(user, "BILL_ANOMALY", "تنبيه: استهلاك غير طبيعي في فاتورة " + typeAr, message);
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

    public void sendSubscriptionPendingPaymentNotification(User user, String planName) {

        if (user == null) {
            throw new ApiException("User not found");
        }

        String message = "تم اختيار خطة " + planName + ". يرجى إكمال عملية الدفع لتفعيل الاشتراك.";

        sendNotification(
                user,
                "SUBSCRIPTION_PENDING_PAYMENT",
                "اشتراك بانتظار الدفع",
                message
        );
    }

    public void sendSubscriptionActivatedNotification(User user, String planName) {

        if (user == null) {
            throw new ApiException("User not found");
        }

        String message = "تم تفعيل اشتراكك في خطة " + planName + " بنجاح. يمكنك الآن استخدام مزايا الخطة.";

        sendNotification(
                user,
                "SUBSCRIPTION_ACTIVATED",
                "تم تفعيل الاشتراك",
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

            case "BILL_ANOMALY":
                subject = "تنبيه: استهلاك غير طبيعي في فاتورتك";
                break;

            case "BILL_DUE":
                subject = "تذكير: فاتورة تستحق قريباً";
                break;

            case "WARRANTY_EXPIRY":
                subject = "تنبيه: ضمان منتج على وشك الانتهاء";
                break;

            case "SUBSCRIPTION_PENDING_PAYMENT":
                subject = "اشتراك بانتظار الدفع";
                break;

            case "SUBSCRIPTION_ACTIVATED":
                subject = "تم تفعيل اشتراكك في دار";
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
        notification.setIsRead(false);
        notification.setSentAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    private String buildEmailTemplate(String title, String message) {

        String logoUrl = "https://res.cloudinary.com/dhpibz1yq/image/upload/f_auto,q_auto/logo_2_f3xxjb";

        return """
        <div dir="rtl" style="margin:0; padding:0; background-color:#E8DED2; font-family: Arial, Tahoma, sans-serif;">
            <div style="max-width:620px; margin:auto; padding:24px 14px;">
                
                <div style="
                    background-color:#FFF9F5;
                    border-radius:20px;
                    overflow:hidden;
                    border:1px solid #D8C2B5;
                    box-shadow:0 8px 24px rgba(59,36,28,0.12);
                ">

                    <!-- Header -->
                    <div style="
                        padding:22px 26px;
                        background:linear-gradient(135deg, #FFF9F5 0%%, #F3E4DC 100%%);
                        border-bottom:1px solid #E1CFC4;
                    ">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                            <tr>
                                <td style="text-align:right; vertical-align:middle;">
                                    <div style="
                                        display:inline-block;
                                        background-color:#F1DCD2;
                                        color:#4A2F25;
                                        padding:6px 14px;
                                        border-radius:999px;
                                        font-size:13px;
                                        font-weight:bold;
                                        margin-bottom:10px;
                                    ">
                                        إشعار من منصة دار
                                    </div>

                                    <h1 style="
                                        margin:0;
                                        color:#3B241C;
                                        font-size:30px;
                                        font-weight:900;
                                    ">
                                        دار
                                    </h1>

                                    <p style="
                                        margin:6px 0 0;
                                        color:#5A3A2E;
                                        font-size:14px;
                                        font-weight:bold;
                                    ">
                                        منصة ذكية لصيانة منزلك
                                    </p>
                                </td>

                                <td style="width:88px; text-align:left; vertical-align:middle;">
                                    <img src="%s" alt="DAR Logo" style="
                                        width:72px;
                                        height:72px;
                                        object-fit:contain;
                                        display:block;
                                        margin-right:auto;
                                        border-radius:16px;
                                    ">
                                </td>
                            </tr>
                        </table>
                    </div>

                    <!-- Body -->
                    <div style="padding:30px 28px 26px; text-align:right;">
                        <h2 style="
                            color:#3B241C;
                            font-size:23px;
                            margin:0 0 16px;
                            font-weight:900;
                            line-height:1.6;
                        ">
                            %s
                        </h2>

                        <p style="
                            color:#241713;
                            font-size:16px;
                            line-height:2;
                            margin:0;
                            font-weight:500;
                        ">
                            %s
                        </p>

                        <div style="
                            margin-top:24px;
                            background-color:#FFFFFF;
                            border:1px solid #E0CDC2;
                            border-radius:16px;
                            padding:16px 18px;
                        ">
                            <p style="
                                margin:0;
                                color:#3B241C;
                                font-size:14px;
                                line-height:1.9;
                                font-weight:bold;
                            ">
                                يمكنك متابعة الصيانة، الفواتير، التنبيهات، وتفاصيل منزلك من خلال منصة دار.
                            </p>
                        </div>
                    </div>

                    <!-- Footer -->
                    <div style="
                        background-color:#3B241C;
                        padding:16px 24px;
                        text-align:center;
                    ">
                        <p style="
                            margin:0;
                            color:#F4E9DF;
                            font-size:13px;
                            font-weight:bold;
                        ">
                            © 2026 دار. جميع الحقوق محفوظة.
                        </p>
                        <p style="
                            margin:7px 0 0;
                            color:#D8BFAF;
                            font-size:12px;
                        ">
                            هذه رسالة تلقائية من منصة دار
                        </p>
                    </div>

                </div>
            </div>
        </div>
        """.formatted(logoUrl, title, message);
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

