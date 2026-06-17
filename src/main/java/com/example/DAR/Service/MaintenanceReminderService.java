package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.MaintenanceReminderDTOIn;
import com.example.DAR.DTO.Out.MaintenanceReminderSummaryDTOOut;
import com.example.DAR.DTO.Out.MaintenanceReminderDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Model.MaintenanceReminder;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.MaintenanceReminderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceReminderService {

    private final MaintenanceReminderRepository maintenanceReminderRepository;
    private final HomeRepository homeRepository;
    private final HomeItemRepository homeItemRepository;
    private final ModelMapper modelMapper;

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

    public void addMaintenanceReminder(Integer home_id, Integer homeItem_id, MaintenanceReminderDTOIn maintenanceReminderDTOIn) {
        Home home = homeRepository.findHomeById(home_id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(homeItem_id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        MaintenanceReminder reminder = new MaintenanceReminder();
        reminder.setTitle(maintenanceReminderDTOIn.getTitle());
        reminder.setMessage(maintenanceReminderDTOIn.getMessage());
        reminder.setReminderDate(maintenanceReminderDTOIn.getReminderDate());
        reminder.setSeason(maintenanceReminderDTOIn.getSeason());
        reminder.setWeatherCondition(maintenanceReminderDTOIn.getWeatherCondition());
        reminder.setIsSent(false);
        reminder.setCreatedAt(LocalDate.now());
        reminder.setHome(home);
        reminder.setHomeItem(homeItem);
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
        reminder.setTitle(maintenanceReminderDTOIn.getTitle());
        reminder.setMessage(maintenanceReminderDTOIn.getMessage());
        reminder.setReminderDate(maintenanceReminderDTOIn.getReminderDate());
        reminder.setSeason(maintenanceReminderDTOIn.getSeason());
        reminder.setWeatherCondition(maintenanceReminderDTOIn.getWeatherCondition());
        reminder.setHome(home);
        reminder.setHomeItem(homeItem);
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

    public void sendReminder(Integer reminderId) {

        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(reminderId);

        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }

        reminder.setIsSent(true);

        maintenanceReminderRepository.save(reminder);
    }

    public void reactivateReminder(Integer reminderId) {

        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(reminderId);

        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }

        reminder.setIsSent(false);

        maintenanceReminderRepository.save(reminder);
    }

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
    
}
