package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.HomeDTOIn;
import com.example.DAR.DTO.Out.HomeDTOOut;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.User;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.NotificationRepository;
import com.example.DAR.Repository.UserRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private static final int FREE_MAX_HOMES = 1;

    private final HomeRepository homeRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    public List<HomeDTOOut> getAll() {
        List<Home> homes = homeRepository.findAll();

        return homes.stream().map(h -> modelMapper.map(h, HomeDTOOut.class)).toList();
    }

    public void addHome(Integer userId, HomeDTOIn homeDTOIn) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(userId, UserSubscriptionStatus.ACTIVE);
        int maxHomes = subscription == null ? FREE_MAX_HOMES : subscription.getSubscriptionPlan().getMaxHomes();
        if (homeRepository.findHomesByUserId(userId).size() >= maxHomes) {
            throw new ApiException("You have reached the maximum number of homes for your plan");
        }
        Home home = new Home();
        home.setName(homeDTOIn.getName());
        home.setAddress(homeDTOIn.getAddress());
        home.setLatitude(homeDTOIn.getLatitude());
        home.setLongitude(homeDTOIn.getLongitude());
        home.setBuildYear(homeDTOIn.getBuildYear());
        home.setCity(homeDTOIn.getCity());
        home.setPropertyType(homeDTOIn.getPropertyType());
        home.setUser(user);
        // Send a one-time free AI smart tip after the user adds a home.
        Home savedHome = homeRepository.save(home);
        notificationService.sendFreeSmartTipUpsellNotification(savedHome.getId());

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
        if (!home.getUser().getId().equals(userId)) {
            throw new ApiException("You do not own this home");
        }
        home.setName(homeDTOIn.getName());
        home.setAddress(homeDTOIn.getAddress());
        home.setLatitude(homeDTOIn.getLatitude());
        home.setLongitude(homeDTOIn.getLongitude());
        home.setBuildYear(homeDTOIn.getBuildYear());
        home.setCity(homeDTOIn.getCity());
        home.setPropertyType(homeDTOIn.getPropertyType());
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
}
