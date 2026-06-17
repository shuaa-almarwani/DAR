package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.MaintenanceDTOIn;
import com.example.DAR.DTO.Out.MaintenanceDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Model.Maintenance;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final HomeRepository homeRepository;
    private final HomeItemRepository homeItemRepository;

    public List<MaintenanceDTOOut> getAll() {
        List<MaintenanceDTOOut> maintenanceDTOOuts = new ArrayList<>();
        for (Maintenance maintenance : maintenanceRepository.findAll()) {
            maintenanceDTOOuts.add(convertToDTO(maintenance));
        }
        return maintenanceDTOOuts;
    }

    public MaintenanceDTOOut getMaintenance(Integer id) {
        Maintenance maintenance = maintenanceRepository.findMaintenanceById(id);
        if (maintenance == null) {
            throw new ApiException("Maintenance not found");
        }
        return convertToDTO(maintenance);
    }

    public List<MaintenanceDTOOut> getMaintenancesByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<MaintenanceDTOOut> maintenanceDTOOuts = new ArrayList<>();
        for (Maintenance maintenance : maintenanceRepository.findMaintenancesByHomeId(homeId)) {
            maintenanceDTOOuts.add(convertToDTO(maintenance));
        }
        return maintenanceDTOOuts;
    }

    public List<MaintenanceDTOOut> getMaintenancesByStatus(String status) {
        List<MaintenanceDTOOut> maintenanceDTOOuts = new ArrayList<>();
        for (Maintenance maintenance : maintenanceRepository.findMaintenancesByStatus(status)) {
            maintenanceDTOOuts.add(convertToDTO(maintenance));
        }
        return maintenanceDTOOuts;
    }

    public List<MaintenanceDTOOut> getMaintenancesByPriority(String priority) {
        List<MaintenanceDTOOut> maintenanceDTOOuts = new ArrayList<>();
        for (Maintenance maintenance : maintenanceRepository.findMaintenancesByPriority(priority)) {
            maintenanceDTOOuts.add(convertToDTO(maintenance));
        }
        return maintenanceDTOOuts;
    }

    public void addMaintenance(Integer home_id, Integer homeItem_id, MaintenanceDTOIn maintenanceDTOIn) {
        Home home = homeRepository.findHomeById(home_id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(homeItem_id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        Maintenance maintenance = new Maintenance();
        maintenance.setTitle(maintenanceDTOIn.getTitle());
        maintenance.setDescription(maintenanceDTOIn.getDescription());
        maintenance.setMaintenanceDate(maintenanceDTOIn.getMaintenanceDate());
        maintenance.setCost(maintenanceDTOIn.getCost());
        maintenance.setStatus(maintenanceDTOIn.getStatus());
        maintenance.setPriority(maintenanceDTOIn.getPriority());
        maintenance.setNotes(maintenanceDTOIn.getNotes());
        maintenance.setHome(home);
        maintenance.setHomeItem(homeItem);
        maintenanceRepository.save(maintenance);
    }

    public void updateMaintenance(Integer id, MaintenanceDTOIn maintenanceDTOIn) {
        Maintenance maintenance = maintenanceRepository.findMaintenanceById(id);
        if (maintenance == null) {
            throw new ApiException("Maintenance not found");
        }
        Home home = homeRepository.findHomeById(maintenance.getHome().getId());
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(maintenance.getHome().getId());
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        maintenance.setTitle(maintenanceDTOIn.getTitle());
        maintenance.setDescription(maintenanceDTOIn.getDescription());
        maintenance.setMaintenanceDate(maintenanceDTOIn.getMaintenanceDate());
        maintenance.setCost(maintenanceDTOIn.getCost());
        maintenance.setStatus(maintenanceDTOIn.getStatus());
        maintenance.setPriority(maintenanceDTOIn.getPriority());
        maintenance.setNotes(maintenanceDTOIn.getNotes());
        maintenance.setHome(home);
        maintenance.setHomeItem(homeItem);
        maintenanceRepository.save(maintenance);
    }

    public void deleteMaintenance(Integer id) {
        Maintenance maintenance = maintenanceRepository.findMaintenanceById(id);
        if (maintenance == null) {
            throw new ApiException("Maintenance not found");
        }
        maintenanceRepository.deleteById(id);
    }

    public MaintenanceDTOOut convertToDTO(Maintenance maintenance) {
        MaintenanceDTOOut maintenanceDTOOut = new MaintenanceDTOOut();
        maintenanceDTOOut.setId(maintenance.getId());
        maintenanceDTOOut.setTitle(maintenance.getTitle());
        maintenanceDTOOut.setDescription(maintenance.getDescription());
        maintenanceDTOOut.setMaintenanceDate(maintenance.getMaintenanceDate());
        maintenanceDTOOut.setCost(maintenance.getCost());
        maintenanceDTOOut.setStatus(maintenance.getStatus());
        maintenanceDTOOut.setPriority(maintenance.getPriority());
        maintenanceDTOOut.setNotes(maintenance.getNotes());
        maintenanceDTOOut.setHomeAddress(maintenance.getHome().getAddress());
        maintenanceDTOOut.setHomeItemCategory(maintenance.getHomeItem().getCategory());
        maintenanceDTOOut.setHomeItemBrand(maintenance.getHomeItem().getBrand());
        return maintenanceDTOOut;
    }
}
