package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeItemDTOIn;
import com.example.DAR.DTO.Out.HomeItemDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.HomeItem;
import com.example.DAR.Repository.HomeItemRepository;
import com.example.DAR.Repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeItemService {

    private final HomeItemRepository homeItemRepository;
    private final HomeRepository homeRepository;
    private final ModelMapper modelMapper;

    public List<HomeItemDTOOut> getAll() {
        List<HomeItem> homeItems = homeItemRepository.findAll();

        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public HomeItemDTOOut getHomeItem(Integer id) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        return modelMapper.map(homeItem, HomeItemDTOOut.class);
    }

    public List<HomeItemDTOOut> getHomeItemsByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        List<HomeItem> homeItems = homeItemRepository.findHomeItemsByHomeId(homeId);

        return homeItems.stream().map(h -> modelMapper.map(h, HomeItemDTOOut.class)).toList();
    }

    public void addHomeItem(Integer homeId, HomeItemDTOIn homeItemDTOIn) {
        Home home = homeRepository.findHomeById(homeId);
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

    public void updateHomeItem(Integer id, Integer homeId, HomeItemDTOIn homeItemDTOIn) {
        HomeItem homeItem = homeItemRepository.findHomeItemById(id);
        if (homeItem == null) {
            throw new ApiException("Home item not found");
        }
        Home home = homeRepository.findHomeById(homeId);
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
}
