package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.MaintenanceReminderDTOIn;
import com.example.DAR.DTO.Out.MaintenanceReminderSummaryDTOOut;
import com.example.DAR.DTO.Out.MaintenanceReminderDTOOut;
import com.example.DAR.Model.*;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.MaintenanceReminderRepository;
import com.example.DAR.Repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
@Service
@RequiredArgsConstructor
public class MaintenanceReminderService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.voice.from}")
    private String voiceFrom;

    @Value("${twilio.voice.twiml-url}")
    private String twimlUrl;
    /// /

    private final MaintenanceReminderRepository maintenanceReminderRepository;
    private final HomeRepository homeRepository;
    private final HomeItemRepository homeItemRepository;
    private final ModelMapper modelMapper;
    private final WeatherService weatherService;
    private final OpenAIService openAIService;
    private final MaintenanceRepository maintenanceRepository;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final NotificationService notificationService;

    public List<MaintenanceReminderDTOOut> getAll() {

        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findAll();

        return reminders.stream().map(m -> modelMapper.map(m, MaintenanceReminderDTOOut.class)).toList();
    }

    public MaintenanceReminderDTOOut getMaintenanceReminder(Integer id) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(id);
        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }
        return modelMapper.map(reminder, MaintenanceReminderDTOOut.class);
    }

    public List<MaintenanceReminderDTOOut> getRemindersByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findMaintenanceRemindersByHomeId(homeId);

        return reminders.stream().map(m -> modelMapper.map(m, MaintenanceReminderDTOOut.class)).toList();
    }

    public List<MaintenanceReminderDTOOut> getUnsentReminders() {
        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findMaintenanceRemindersByIsSent(false);

        return reminders.stream().map(m -> modelMapper.map(m, MaintenanceReminderDTOOut.class)).toList();
    }

    public List<MaintenanceReminderDTOOut> getRemindersBySeason(String season) {
        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findMaintenanceRemindersBySeason(season);

        return reminders.stream().map(m -> modelMapper.map(m, MaintenanceReminderDTOOut.class)).toList();
    }

    // Creates a reminder from a maintenance task and lets AI improve the message when possible.
    public void addMaintenanceReminder(Integer maintenanceId, MaintenanceReminderDTOIn dto) {

        Maintenance maintenance = maintenanceRepository.findMaintenanceById(maintenanceId);

        if (maintenance == null) {
            throw new ApiException("Maintenance not found");
        }

        MaintenanceReminder reminder = new MaintenanceReminder();

        reminder.setMaintenance(maintenance);
        reminder.setHome(maintenance.getHome());
        reminder.setHomeItem(maintenance.getHomeItem());
        reminder.setTitle(dto.getTitle());
        //
        String reminderMessage;

        try {
            reminderMessage = generateReminderMessageWithAI(maintenance, dto);
        } catch (Exception e) {
            reminderMessage = dto.getMessage();
        }

        reminder.setMessage(reminderMessage);
        //
        reminder.setReminderDate(dto.getReminderDate());
        reminder.setSeason(dto.getSeason());
        reminder.setWeatherCondition(dto.getWeatherCondition());
        reminder.setCreatedAt(LocalDate.now());
        reminder.setIsSent(false);
        reminder.setNotificationMethod(dto.getNotificationMethod());

        maintenanceReminderRepository.save(reminder);
    }

    public void updateMaintenanceReminder(Integer id, Integer home_id, Integer homeItem_id, MaintenanceReminderDTOIn maintenanceReminderDTOIn) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(id);
        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }
        Home home = homeRepository.findHomeById(home_id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(homeItem_id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        if (!reminder.getHome().getId().equals(home_id)) {
            throw new ApiException("Reminder does not belong to this home");
        }

        if (!homeItem.getHome().getId().equals(home_id)) {
            throw new ApiException("Home item does not belong to this home");
        }
        reminder.setTitle(maintenanceReminderDTOIn.getTitle());
        reminder.setMessage(maintenanceReminderDTOIn.getMessage());
        reminder.setReminderDate(maintenanceReminderDTOIn.getReminderDate());
        reminder.setSeason(maintenanceReminderDTOIn.getSeason());
        reminder.setWeatherCondition(maintenanceReminderDTOIn.getWeatherCondition());
        reminder.setHome(home);
        reminder.setHomeItem(homeItem);
        reminder.setCreatedAt(LocalDate.now());
        reminder.setNotificationMethod(maintenanceReminderDTOIn.getNotificationMethod());
        maintenanceReminderRepository.save(reminder);

    }

    public void markAsSent(Integer id) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(id);
        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }
        reminder.setIsSent(true);
        maintenanceReminderRepository.save(reminder);
    }

    public void deleteMaintenanceReminder(Integer id) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(id);
        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }
        maintenanceReminderRepository.deleteById(id);
    }


    public List<MaintenanceReminderDTOOut> getUpcomingReminders(Integer homeId) {

        Home home = homeRepository.findHomeById(homeId);

        if (home == null) {
            throw new ApiException("Home not found");
        }

        LocalDate today = LocalDate.now();

        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findMaintenanceRemindersByHomeId(homeId);

        return reminders.stream()
                .filter(r -> !r.getIsSent())
                .filter(r -> r.getReminderDate().isEqual(today) || r.getReminderDate().isAfter(today))
                .map(r -> modelMapper.map(r, MaintenanceReminderDTOOut.class))
                .toList();
    }

    public List<MaintenanceReminderDTOOut> getTodayReminders(Integer homeId) {

        Home home = homeRepository.findHomeById(homeId);

        if (home == null) {
            throw new ApiException("Home not found");
        }

        LocalDate today = LocalDate.now();

        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findMaintenanceRemindersByHomeId(homeId);

        return reminders.stream()
                .filter(r -> !r.getIsSent())
                .filter(r -> r.getReminderDate().isEqual(today))
                .map(r -> modelMapper.map(r, MaintenanceReminderDTOOut.class))
                .toList();
    }

    // Sends the reminder using the selected method, then marks it as sent.
    public void sendReminder(Integer reminderId) {

        MaintenanceReminder reminder =
                maintenanceReminderRepository.findMaintenanceReminderById(reminderId);

        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }

        if (reminder.getIsSent()) {
            throw new ApiException("Maintenance reminder already sent");
        }

        User user = reminder.getHome().getUser();

        if (user == null) {
            throw new ApiException("User not found");
        }

        String method = reminder.getNotificationMethod().toUpperCase();

        switch (method) {

            case "EMAIL":
                sendReminderByEmail(user, reminder);
                break;

            case "WHATSAPP":
                sendReminderByWhatsapp(user, reminder);
                break;

            case "CALL":
                callUserForReminder(user, reminder);
                break;

            default:
                throw new ApiException("Invalid notification method");
        }

        reminder.setIsSent(true);
//        if (reminder.getMaintenance().getPriority().equalsIgnoreCase("URGENT")
//                && !method.equals("CALL")) {
//            callUserForReminder(user, reminder);
//        }
        // شيلي التعليق قبل العرض
        maintenanceReminderRepository.save(reminder);
    }

    //@Scheduled(cron = "0 0 8 * * *")
    @Scheduled(cron = " 0 * * * * *")
    // للتجربة بينرسل كل دقيقة

    // Scheduler that sends today's unsent reminders automatically.
    public void sendTodayRemindersAutomatically() {

        LocalDate today = LocalDate.now();

        List<MaintenanceReminder> reminders =
                maintenanceReminderRepository.findMaintenanceRemindersByReminderDateAndIsSent(today, false);

        for (MaintenanceReminder reminder : reminders) {
            try {
                sendReminder(reminder.getId());
            } catch (Exception e) {
                System.out.println("Reminder not sent. ID: " + reminder.getId());
                System.out.println(e.getMessage());
            }
        }
    }

    // helper method to send
    // Builds and sends the reminder email.
    private void sendReminderByEmail(User user, MaintenanceReminder reminder) {

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ApiException("User email not found");
        }

        try {
            String subject = "تذكير صيانة من دار";

            String priorityBadge = getPriorityBadge(reminder.getMaintenance().getPriority());

            String message =
                    priorityBadge +
                            "<br>" +
                            "مرحبًا " + user.getName() + "،<br><br>" +
                            "لديك تذكير صيانة:<br>" +
                            "<b>" + reminder.getTitle() + "</b><br><br>" +
                            reminder.getMessage() + "<br><br>" +
                            "تاريخ التذكير: " + reminder.getReminderDate();

            String htmlMessage =
                    notificationService.buildEmailTemplate(subject, message);

            emailService.sendEmail(
                    user.getEmail(),
                    subject,
                    htmlMessage
            );

        } catch (Exception e) {
            System.out.println("Email reminder not sent: " + e.getMessage());
            throw new ApiException("Email reminder not sent");
        }
    }

    // Builds and sends the reminder WhatsApp message.
    private void sendReminderByWhatsapp(User user, MaintenanceReminder reminder) {

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new ApiException("User phone number not found");
        }

        String priorityText = getPriorityText(reminder.getMaintenance().getPriority());

        String message =
                "تذكير صيانة من دار\n\n" +
                        priorityText + "\n" +
                        "مرحبًا " + user.getName() + "\n" +
                        "لديك تذكير صيانة: " + reminder.getTitle() + "\n" +
                        reminder.getMessage() + "\n" +
                        "تاريخ التذكير: " + reminder.getReminderDate();

        try {
            whatsAppService.whatsAppMessage(
                    user.getPhoneNumber(),
                    message
            );

        } catch (Exception e) {
            System.out.println("WhatsApp reminder not sent: " + e.getMessage());
            throw new ApiException("WhatsApp reminder not sent");
        }
    }

    // helper method to sendReminderByWhatsapp
    private String getPriorityText(String priority) {

        if (priority == null) {
            return "الأولوية: غير محددة";
        }

        return switch (priority.toUpperCase()) {
            case "URGENT" -> "الأولوية: عاجلة";
            case "HIGH" -> "الأولوية: عالية";
            case "MEDIUM" -> "الأولوية: متوسطة";
            case "LOW" -> "الأولوية: منخفضة";
            default -> "الأولوية: غير محددة";
        };
    }

    // Starts a Twilio voice call for urgent reminders.
    private void callUserForReminder(User user, MaintenanceReminder reminder) {

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new ApiException("User phone number not found");
        }

        String formattedPhone = user.getPhoneNumber().trim();

        formattedPhone = formattedPhone.replace("whatsapp:", "");
        formattedPhone = formattedPhone.replace("WhatsApp:", "");
        formattedPhone = formattedPhone.replace("WHATSAPP:", "");

        if (formattedPhone.startsWith("05")) {
            formattedPhone = "+966" + formattedPhone.substring(1);
        } else if (formattedPhone.startsWith("5")) {
            formattedPhone = "+966" + formattedPhone;
        }

        Twilio.init(accountSid, authToken);

        Call call = Call.creator(
                new PhoneNumber(formattedPhone),
                new PhoneNumber(voiceFrom),
                URI.create(twimlUrl)
        ).create();

        System.out.println("Maintenance reminder call started. SID: " + call.getSid());
    }

    public void reactivateReminder(Integer reminderId) {

        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(reminderId);

        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }

        reminder.setIsSent(false);

        maintenanceReminderRepository.save(reminder);
    }

    // Counts sent, unsent, today, and upcoming reminders for a home.
    public MaintenanceReminderSummaryDTOOut getReminderSummary(Integer homeId) {

        Home home = homeRepository.findHomeById(homeId);

        if (home == null) {
            throw new ApiException("Home not found");
        }

        LocalDate today = LocalDate.now();

        List<MaintenanceReminder> reminders = maintenanceReminderRepository.findMaintenanceRemindersByHomeId(homeId);

        int totalReminders = reminders.size();

        int sentReminders = (int) reminders.stream()
                .filter(MaintenanceReminder::getIsSent)
                .count();

        int unsentReminders = (int) reminders.stream()
                .filter(r -> !r.getIsSent())
                .count();

        int todayReminders = (int) reminders.stream()
                .filter(r -> !r.getIsSent())
                .filter(r -> r.getReminderDate().isEqual(today))
                .count();

        int upcomingReminders = (int) reminders.stream()
                .filter(r -> !r.getIsSent())
                .filter(r -> r.getReminderDate().isEqual(today) || r.getReminderDate().isAfter(today))
                .count();

        return new MaintenanceReminderSummaryDTOOut(
                totalReminders,
                upcomingReminders,
                todayReminders,
                sentReminders,
                unsentReminders
        );
    }

    // weather and ai
    // #1
    // Generates short maintenance advice based on current city weather.
    public String getAIWeatherMaintenanceAdvice(Integer homeId) {

        Home home = homeRepository.findHomeById(homeId);

        if (home == null) {
            throw new ApiException("Home not found");
        }

        String weatherDescription = weatherService.getWeatherDescription(home.getCity());

        String prompt = """
                You are an AI assistant for a smart Arabic home maintenance platform called DAR.
                
                The platform does not perform maintenance.
                It only gives reminders and suggestions to help the user take care of the home.
                
                Based on this weather information:
                %s
                
                Give a short Arabic maintenance advice for the homeowner.
                The advice should be related to home maintenance reminders only.
                Examples:
                - AC filter cleaning in hot weather
                - water heater check in cold weather
                - humidity or leakage check in rainy/humid weather
                
                Return the answer in Arabic only.
                Keep it short and user-friendly.
                """.formatted(weatherDescription);

        return openAIService.generateReaderAnalysis(prompt);
    }

    // helper
    private String getPriorityBadge(String priority) {

        if (priority == null) {
            return """
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
                        أولوية غير محددة
                    </div>
                    """;
        }

        switch (priority.toUpperCase()) {

            case "URGENT":
                return """
                        <div style="
                            display:inline-block;
                            background-color:#FDE2E2;
                            color:#8A1F1F;
                            padding:6px 14px;
                            border-radius:999px;
                            font-size:13px;
                            font-weight:bold;
                            margin-bottom:10px;
                        ">
                            أولوية عاجلة
                        </div>
                        """;

            case "HIGH":
                return """
                        <div style="
                            display:inline-block;
                            background-color:#FFE8CC;
                            color:#8A4B00;
                            padding:6px 14px;
                            border-radius:999px;
                            font-size:13px;
                            font-weight:bold;
                            margin-bottom:10px;
                        ">
                            أولوية عالية
                        </div>
                        """;

            case "MEDIUM":
                return """
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
                            أولوية متوسطة
                        </div>
                        """;

            case "LOW":
                return """
                        <div style="
                            display:inline-block;
                            background-color:#E6F4EA;
                            color:#1E6B3A;
                            padding:6px 14px;
                            border-radius:999px;
                            font-size:13px;
                            font-weight:bold;
                            margin-bottom:10px;
                        ">
                            أولوية منخفضة
                        </div>
                        """;

            default:
                return """
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
                            أولوية غير محددة
                        </div>
                        """;
        }
    }

    // Creates a friendly Arabic reminder message from maintenance and weather data.
    private String generateReminderMessageWithAI(Maintenance maintenance, MaintenanceReminderDTOIn dto) {

        String weatherDescription = weatherService.getWeatherDescription(maintenance.getHome().getCity());

        String prompt = """
                You are an AI assistant for DAR, a smart Arabic home maintenance platform.
                
                DAR does not perform maintenance.
                DAR helps users remember and organize home maintenance tasks.
                
                Create a short Arabic maintenance reminder message based on the following data:
                
                Maintenance title: %s
                Maintenance description: %s
                Home item: %s
                Season: %s
                Weather trigger selected by user: %s
                Current weather: %s
                
                Requirements:
                - Write in Arabic only.
                - Keep it short and friendly.
                - Make the message practical and related to home care.
                - Do not say that DAR will perform the maintenance.
                - Tell the user what to check or remember.
                - Return only the reminder message.
                """.formatted(
                maintenance.getTitle(),
                maintenance.getDescription(),
                maintenance.getHomeItem().getCategory(),
                dto.getSeason(),
                dto.getWeatherCondition(),
                weatherDescription
        );

        return openAIService.generateReaderAnalysis(prompt);
    }
}
