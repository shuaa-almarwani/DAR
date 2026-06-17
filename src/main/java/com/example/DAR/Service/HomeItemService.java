package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeItemDTOIn;
import com.example.DAR.DTO.Out.HomeItemDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeItemService {

    private final HomeItemRepository homeItemRepository;
    private final HomeRepository homeRepository;

    public List<HomeItemDTOOut> getAll() {
        List<HomeItemDTOOut> homeItemDTOOuts = new ArrayList<>();
        for (HomeItem homeItem : homeItemRepository.findAll()) {
            homeItemDTOOuts.add(convertToDTO(homeItem));
        }
        return homeItemDTOOuts;
    }

    public HomeItemDTOOut getHomeItem(Integer id) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        return convertToDTO(homeItem);
    }

    public List<HomeItemDTOOut> getHomeItemsByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItemDTOOut> homeItemDTOOuts = new ArrayList<>();
        for (HomeItem homeItem : homeItemRepository.findHomeItemsByHomeId(homeId)) {
            homeItemDTOOuts.add(convertToDTO(homeItem));
        }
        return homeItemDTOOuts;
    }

    public void addHomeItem(Integer home_id, HomeItemDTOIn homeItemDTOIn) {
        Home home = homeRepository.findHomeById(home_id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        HomeItem homeItem = new HomeItem();
        homeItem.setCategory(homeItemDTOIn.getCategory());
        homeItem.setBrand(homeItemDTOIn.getBrand());
        homeItem.setInstallDate(homeItemDTOIn.getInstallDate());
        homeItem.setLifespanMonth(homeItemDTOIn.getLifespanMonth());
        homeItem.setNextServiceDate(homeItemDTOIn.getNextServiceDate());
        homeItem.setNotes(homeItemDTOIn.getNotes());
        homeItem.setHome(home);
        homeItemRepository.save(homeItem);
    }

    public void updateHomeItem(Integer id, HomeItemDTOIn homeItemDTOIn) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        Home home = homeRepository.findHomeById(homeItem.getHome().getId());
        if (home == null) {
            throw new ApiException("Home not found");
        }
        homeItem.setCategory(homeItemDTOIn.getCategory());
        homeItem.setBrand(homeItemDTOIn.getBrand());
        homeItem.setInstallDate(homeItemDTOIn.getInstallDate());
        homeItem.setLifespanMonth(homeItemDTOIn.getLifespanMonth());
        homeItem.setNextServiceDate(homeItemDTOIn.getNextServiceDate());
        homeItem.setNotes(homeItemDTOIn.getNotes());
        homeItem.setHome(home);
        homeItemRepository.save(homeItem);
    }

    public void deleteHomeItem(Integer id) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        homeItemRepository.deleteById(id);
    }

    public HomeItemDTOOut convertToDTO(HomeItem homeItem) {
        HomeItemDTOOut homeItemDTOOut = new HomeItemDTOOut();
        homeItemDTOOut.setId(homeItem.getId());
        homeItemDTOOut.setCategory(homeItem.getCategory());
        homeItemDTOOut.setBrand(homeItem.getBrand());
        homeItemDTOOut.setInstallDate(homeItem.getInstallDate());
        homeItemDTOOut.setLifespanMonth(homeItem.getLifespanMonth());
        homeItemDTOOut.setNextServiceDate(homeItem.getNextServiceDate());
        homeItemDTOOut.setNotes(homeItem.getNotes());
        homeItemDTOOut.setHomeAddress(homeItem.getHome().getAddress());
        return homeItemDTOOut;
    }
}
