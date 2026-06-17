package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.DTO.Out.HomeDTOOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<HomeDTOOut> getAll() {
        List<Home> homes = homeRepository.findAll();

        return homes.stream().map(h -> modelMapper.map(h, HomeDTOOut.class)).toList();
    }

    public HomeDTOOut getHome(Integer id) {
        Home home = homeRepository.findHomeById(id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        return modelMapper.map(home, HomeDTOOut.class);
    }

    public List<HomeDTOOut> getHomesByUser(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        List<Home> homes = homeRepository.findHomesByUserId(userId);

        return homes.stream().map(h -> modelMapper.map(h, HomeDTOOut.class)).toList();
    }

    public void addHome(Integer userId, HomeDTOIn homeDTOIn) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        Home home = new Home();
        home.setAddress(homeDTOIn.getAddress());
        home.setLatitude(homeDTOIn.getLatitude());
        home.setLongitude(homeDTOIn.getLongitude());
        home.setBuildYear(homeDTOIn.getBuildYear());
        home.setCity(homeDTOIn.getCity());
        home.setUser(user);
        homeRepository.save(home);
    }

    public void updateHome(Integer id, Integer userId, HomeDTOIn homeDTOIn) {
        Home home = homeRepository.findHomeById(id);
        if (home == null) {
            throw new ApiException("Home not found");
        }
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        home.setAddress(homeDTOIn.getAddress());
        home.setLatitude(homeDTOIn.getLatitude());
        home.setLongitude(homeDTOIn.getLongitude());
        home.setBuildYear(homeDTOIn.getBuildYear());
        home.setCity(homeDTOIn.getCity());
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
}
