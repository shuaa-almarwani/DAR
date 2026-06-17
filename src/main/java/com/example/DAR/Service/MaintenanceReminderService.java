package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.MaintenanceReminderDTOIn;
import com.example.DAR.DTO.Out.MaintenanceReminderDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Model.MaintenanceReminder;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.MaintenanceReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceReminderService {

    private final MaintenanceReminderRepository maintenanceReminderRepository;
    private final HomeRepository homeRepository;
    private final HomeItemRepository homeItemRepository;

    public List<MaintenanceReminderDTOOut> getAll() {
        List<MaintenanceReminderDTOOut> maintenanceReminderDTOOuts = new ArrayList<>();
        for (MaintenanceReminder reminder : maintenanceReminderRepository.findAll()) {
            maintenanceReminderDTOOuts.add(convertToDTO(reminder));
        }
        return maintenanceReminderDTOOuts;
    }

    public MaintenanceReminderDTOOut getMaintenanceReminder(Integer id) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(id);
        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }
        return convertToDTO(reminder);
    }

    public List<MaintenanceReminderDTOOut> getRemindersByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<MaintenanceReminderDTOOut> maintenanceReminderDTOOuts = new ArrayList<>();
        for (MaintenanceReminder reminder : maintenanceReminderRepository.findMaintenanceRemindersByHomeId(homeId)) {
            maintenanceReminderDTOOuts.add(convertToDTO(reminder));
        }
        return maintenanceReminderDTOOuts;
    }

    public List<MaintenanceReminderDTOOut> getUnsentReminders() {
        List<MaintenanceReminderDTOOut> maintenanceReminderDTOOuts = new ArrayList<>();
        for (MaintenanceReminder reminder : maintenanceReminderRepository.findMaintenanceRemindersByIsSent(false)) {
            maintenanceReminderDTOOuts.add(convertToDTO(reminder));
        }
        return maintenanceReminderDTOOuts;
    }

    public List<MaintenanceReminderDTOOut> getRemindersBySeason(String season) {
        List<MaintenanceReminderDTOOut> maintenanceReminderDTOOuts = new ArrayList<>();
        for (MaintenanceReminder reminder : maintenanceReminderRepository.findMaintenanceRemindersBySeason(season)) {
            maintenanceReminderDTOOuts.add(convertToDTO(reminder));
        }
        return maintenanceReminderDTOOuts;
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

    public void updateMaintenanceReminder(Integer id, MaintenanceReminderDTOIn maintenanceReminderDTOIn) {
        MaintenanceReminder reminder = maintenanceReminderRepository.findMaintenanceReminderById(id);
        if (reminder == null) {
            throw new ApiException("Maintenance reminder not found");
        }
        Home home = homeRepository.findHomeById(reminder.getHome().getId());
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(reminder.getHome().getId());
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

    public MaintenanceReminderDTOOut convertToDTO(MaintenanceReminder reminder) {
        MaintenanceReminderDTOOut maintenanceReminderDTOOut = new MaintenanceReminderDTOOut();
        maintenanceReminderDTOOut.setId(reminder.getId());
        maintenanceReminderDTOOut.setTitle(reminder.getTitle());
        maintenanceReminderDTOOut.setMessage(reminder.getMessage());
        maintenanceReminderDTOOut.setReminderDate(reminder.getReminderDate());
        maintenanceReminderDTOOut.setSeason(reminder.getSeason());
        maintenanceReminderDTOOut.setWeatherCondition(reminder.getWeatherCondition());
        maintenanceReminderDTOOut.setIsSent(reminder.getIsSent());
        maintenanceReminderDTOOut.setCreatedAt(reminder.getCreatedAt());
        maintenanceReminderDTOOut.setHomeAddress(reminder.getHome().getAddress());
        maintenanceReminderDTOOut.setHomeItemCategory(reminder.getHomeItem().getCategory());
        maintenanceReminderDTOOut.setHomeItemBrand(reminder.getHomeItem().getBrand());
        return maintenanceReminderDTOOut;
    }
}
