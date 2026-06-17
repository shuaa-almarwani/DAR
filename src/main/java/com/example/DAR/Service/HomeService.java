package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.DTO.Out.HomeDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;

    public List<HomeDTOOut> getAll() {
        List<HomeDTOOut> homeDTOOuts = new ArrayList<>();
        for (Home home : homeRepository.findAll()) {
            homeDTOOuts.add(convertToDTO(home));
        }
        return homeDTOOuts;
    }

    public HomeDTOOut getHome(Integer id) {
        Home home = homeRepository.findHomeById(id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        return convertToDTO(home);
    }

    public List<HomeDTOOut> getHomesByUser(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        List<HomeDTOOut> homeDTOOuts = new ArrayList<>();
        for (Home home : homeRepository.findHomesByUserId(userId)) {
            homeDTOOuts.add(convertToDTO(home));
        }
        return homeDTOOuts;
    }

    public void addHome(Integer user_id, HomeDTOIn homeDTOIn) {
        User user = userRepository.findUserById(user_id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        Home home = new Home();
        home.setAddress(homeDTOIn.getAddress());
        home.setLatitude(homeDTOIn.getLatitude());
        home.setLongitude(homeDTOIn.getLongitude());
        home.setBuildYear(homeDTOIn.getBuildYear());
        home.setUser(user);
        homeRepository.save(home);
    }

    public void updateHome(Integer id, HomeDTOIn homeDTOIn) {
        Home home = homeRepository.findHomeById(id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        User user = userRepository.findUserById(home.getUser().getId());
        if (user == null) {
            throw new ApiException("User not found");
        }
        home.setAddress(homeDTOIn.getAddress());
        home.setLatitude(homeDTOIn.getLatitude());
        home.setLongitude(homeDTOIn.getLongitude());
        home.setBuildYear(homeDTOIn.getBuildYear());
        home.setUser(user);
        homeRepository.save(home);
    }

    public void deleteHome(Integer id) {
        Home home = homeRepository.findHomeById(id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        homeRepository.deleteById(id);
    }

    public HomeDTOOut convertToDTO(Home home) {
        HomeDTOOut homeDTOOut = new HomeDTOOut();
        homeDTOOut.setId(home.getId());
        homeDTOOut.setAddress(home.getAddress());
        homeDTOOut.setLatitude(home.getLatitude());
        homeDTOOut.setLongitude(home.getLongitude());
        homeDTOOut.setBuildYear(home.getBuildYear());
        homeDTOOut.setOwnerName(home.getUser().getName());
        return homeDTOOut;
    }
}
