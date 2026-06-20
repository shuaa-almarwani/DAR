package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.Out.NotificationSummaryDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Model.Notification;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.NotificationRepository;
import com.example.DAR.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final HomeRepository homeRepository;
    private final WeatherService weatherService;
    private final OpenAIService openAIService;
    private final HomeItemRepository homeItemRepository;

    public List<Notification>  getAllNotifications() {
        return  notificationRepository.findAll();
    }

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

    public String buildEmailTemplate(String title, String message) {

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


    public void createWeatherAlertNotification(Integer homeId) {

        Home home = homeRepository.findHomeById(homeId);

        if (home == null) {
            throw new ApiException("Home not found");
        }

        User user = home.getUser();

        if (user == null) {
            throw new ApiException("User not found");
        }

        if (!user.getSmartAlertsEnabled()) {
            throw new ApiException("Smart alerts are not enabled for this user");
        }

        Map<String, Object> weatherData = weatherService.getWeatherData(home.getCity());

        Double temperature = ((Number) weatherData.get("temperature")).doubleValue();
        Integer humidity = ((Number) weatherData.get("humidity")).intValue();
        String description = weatherData.get("description").toString();

        String alertType = getWeatherAlertType(temperature, humidity, description);

        if (alertType == null) {
            throw new ApiException("No weather alert needed today");
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        boolean alreadySentToday =
                notificationRepository.existsNotificationByHomeIdAndTypeAndSentAtBetween(
                        homeId,
                        alertType,
                        startOfDay,
                        endOfDay
                );

        if (alreadySentToday) {
            throw new ApiException("Weather alert already sent today for this home");
        }

        String prompt = """
            You are an AI assistant for a smart Arabic home maintenance platform called DAR.

            The platform does not perform maintenance.
            It only sends smart alerts and reminders to help users take care of their homes.

            Home city: %s
            Weather description: %s
            Temperature: %.1f Celsius
            Humidity: %d

            Alert type: %s

            Write a short Arabic notification message for the homeowner.
            The message should be practical and related to home care or maintenance.
            Return only the Arabic notification message.
            Keep it short, clear, and friendly.
            """.formatted(
                home.getCity(),
                description,
                temperature,
                humidity,
                alertType
        );

        String aiMessage = openAIService.generateReaderAnalysis(prompt);

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setHome(home);
        notification.setTitle("تنبيه ذكي حسب الطقس");
        notification.setMessage(aiMessage);
        notification.setType(alertType);
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);

        notificationRepository.save(notification);
        try {
            String htmlMessage =
                    buildEmailTemplate("تنبيه ذكي حسب الطقس", aiMessage.replace("\n", "<br>"));

            emailService.sendEmail(
                    user.getEmail(),
                    "تنبيه ذكي حسب الطقس",
                    htmlMessage
            );

        } catch (Exception e) {
            System.out.println("Weather alert email not sent: " + e.getMessage());
        }
    }
    // helper method
    private String getWeatherAlertType(Double temperature, Integer humidity, String description) {

        String weatherDescription = description.toLowerCase();

        if (temperature >= 40) {
            return "WEATHER_AC_CHECK";
        }

        if (temperature <= 12) {
            return "WEATHER_HEATER_CHECK";
        }

        if (humidity >= 60) {
            return "WEATHER_HUMIDITY_CHECK";
        }

        if (weatherDescription.contains("rain") || weatherDescription.contains("thunderstorm")) {
            return "WEATHER_LEAK_CHECK";
        }

        if (weatherDescription.contains("wind") || weatherDescription.contains("storm")) {
            return "WEATHER_WINDOW_CHECK";
        }

        return null;
    }
    public void sendSmartAlertIntro(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }
        if (user.getSmartAlertIntroSent()) {
            throw new ApiException("Smart alert introduction already sent");
        }

        String introMessage =
                "هل ترغب بتفعيل التنبيهات الذكية من دار؟ " +
                        "سنرسل لك تنبيهات مبنية على الطقس وحالة المنزل لمساعدتك على العناية بمنزلك.";

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setHome(null);
        notification.setTitle("تفعيل التنبيهات الذكية");
        notification.setMessage(introMessage);
        notification.setType("SMART_ALERT_INTRO");
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);

        notificationRepository.save(notification);

        try {
            String htmlMessage =
                    buildEmailTemplate("تفعيل التنبيهات الذكية", introMessage.replace("\n", "<br>"));

            emailService.sendEmail(
                    user.getEmail(),
                    "تفعيل التنبيهات الذكية",
                    htmlMessage
            );

        } catch (Exception e) {
            System.out.println("Smart alert intro email not sent: " + e.getMessage());
        }

        user.setSmartAlertIntroSent(true);
        userRepository.save(user);
    }


//    @Scheduled(cron = "0 0 8 * * *")
@Scheduled(cron = "0 * * * * *")
public void sendDailyWeatherTipsAutomatically() {

        List<Home> homes = homeRepository.findAll();

        for (Home home : homes) {
            try {
                sendDailyWeatherTip(home.getId());
            } catch (Exception e) {
                System.out.println("Daily weather tip not sent for home ID: " + home.getId());
                System.out.println(e.getMessage());
            }
        }
    }
    public void sendDailyWeatherTip(Integer homeId) {

        Home home = homeRepository.findHomeById(homeId);

        if (home == null) {
            throw new ApiException("Home not found");
        }

        User user = home.getUser();

        if (user == null) {
            throw new ApiException("User not found");
        }

        if (!user.getSmartAlertsEnabled()) {
            throw new ApiException("Smart alerts are disabled");
        }

        String weatherDescription =
                weatherService.getWeatherDescription(home.getCity());

        List<HomeItem> items =
                homeItemRepository.findHomeItemsByHomeId(homeId);

        String itemNames = items.stream()
                .map(HomeItem::getName)
                .toList()
                .toString();

        String prompt = """
            You are an AI assistant for DAR, a smart Arabic home care platform.

            DAR does not perform maintenance.
            DAR gives smart reminders and home care advice.

            Based on today's weather and the user's home items, write one short Arabic home care tip.

            City: %s
            Today's weather: %s
            User home items: %s

            Requirements:
            - Arabic only.
            - Short and practical.
            - Mention only relevant items from the user's home.
            - Do not say DAR will perform maintenance.
            - Give advice the user can do or check.
            - Return only the message.
            """.formatted(
                home.getCity(),
                weatherDescription,
                itemNames
        );

        String aiMessage = openAIService.generateReaderAnalysis(prompt);

        Notification notification = new Notification();
        notification.setTitle("نصيحة يومية للعناية بالمنزل");
        notification.setMessage(aiMessage);
        notification.setType("DAILY_WEATHER_TIP");
        notification.setIsRead(false);
        notification.setSentAt(LocalDateTime.now());
        notification.setHome(home);
        notification.setUser(user);

        notificationRepository.save(notification);

        String htmlMessage = buildEmailTemplate(
                "نصيحة يومية من دار",
                aiMessage
        );

        emailService.sendEmail(
                user.getEmail(),
                "نصيحة يومية من دار",
                htmlMessage
        );
    }
}

