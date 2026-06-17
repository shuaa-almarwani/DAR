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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final HomeRepository homeRepository;
    private final HomeItemRepository homeItemRepository;
    private final ModelMapper modelMapper;

    public List<MaintenanceDTOOut> getAll() {
        List<Maintenance> maintenances = maintenanceRepository.findAll();

        return maintenances.stream().map(m -> modelMapper.map(m, MaintenanceDTOOut.class)).toList();
    }

    public MaintenanceDTOOut getMaintenance(Integer id) {
        Maintenance maintenance = maintenanceRepository.findMaintenanceById(id);
        if (maintenance == null) {
            throw new ApiException("Maintenance not found");
        }
        return modelMapper.map(maintenance, MaintenanceDTOOut.class);
    }

    public List<MaintenanceDTOOut> getMaintenancesByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<Maintenance> maintenances = maintenanceRepository.findMaintenancesByHomeId(homeId);

        return maintenances.stream().map(m -> modelMapper.map(m, MaintenanceDTOOut.class)).toList();
    }

    public List<MaintenanceDTOOut> getMaintenancesByStatus(String status) {
        List<Maintenance> maintenances = maintenanceRepository.findMaintenancesByStatus(status);

        return maintenances.stream().map(m -> modelMapper.map(m, MaintenanceDTOOut.class)).toList();
    }

    public List<MaintenanceDTOOut> getMaintenancesByPriority(String priority) {
        List<Maintenance> maintenances = maintenanceRepository.findMaintenancesByPriority(priority);

        return maintenances.stream().map(m -> modelMapper.map(m, MaintenanceDTOOut.class)).toList();
    }

    public void addMaintenance(Integer homeId, Integer homeItemId, MaintenanceDTOIn maintenanceDTOIn) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(homeItemId);
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

    public void updateMaintenance(Integer id, Integer homeId, Integer homeItemId, MaintenanceDTOIn maintenanceDTOIn) {
        Maintenance maintenance = maintenanceRepository.findMaintenanceById(id);
        if (maintenance == null) {
            throw new ApiException("Maintenance not found");
        }
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = homeItemRepository.findHomeItemById(homeItemId);
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
}
