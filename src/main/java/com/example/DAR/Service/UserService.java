package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.UserDtoIn;
import com.example.DAR.DTO.Out.UserDtoOut;
import com.example.DAR.Model.User;
import com.example.DAR.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
private final NotificationService notificationService;


    public List<UserDtoOut> getAllUsers() {

        List<User> users = userRepository.findAll();
        List<UserDtoOut> userDtoOuts = new ArrayList<>();

        for (User user : users) {
            UserDtoOut dto = modelMapper.map(user, UserDtoOut.class);
            userDtoOuts.add(dto);
        }

        return userDtoOuts;
    }
    public void addUser(UserDtoIn dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ApiException("Username already exists");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already exists");
        }

        User user = modelMapper.map(dto, User.class);
        user.setCreateAt(LocalDate.now());

        userRepository.save(user);
        notificationService.sendWelcomeNotification(user.getId());
    }


    public void updateUser(Integer id, UserDtoIn dto) {

        User oldUser = userRepository.findUserById(id);

        if (oldUser == null) {
            throw new ApiException("User not found");
        }

        if (!oldUser.getUsername().equals(dto.getUsername())
                && userRepository.existsByUsername(dto.getUsername())) {
            throw new ApiException("Username already exists");
        }

        if (!oldUser.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException("Email already exists");
        }

        oldUser.setName(dto.getName());
        oldUser.setEmail(dto.getEmail());
        oldUser.setUsername(dto.getUsername());
        oldUser.setPassword(dto.getPassword());
        oldUser.setPhoneNumber(dto.getPhoneNumber());

        userRepository.save(oldUser);
        notificationService.sendProfileUpdatedNotification(oldUser.getId());
    }
    public void deleteUser(Integer id) {

        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new ApiException("User not found");
        }

        notificationService.sendAccountDeletedNotification(user);
        userRepository.delete(user);
    }

    public UserDtoOut getUserById(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new ApiException("User not found");
        }
        return modelMapper.map(user, UserDtoOut.class);
    }

    public UserDtoOut getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new ApiException("User not found");
        }
        return modelMapper.map(user, UserDtoOut.class);
    }

    public UserDtoOut getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new ApiException("User not found");
        }
        return modelMapper.map(user, UserDtoOut.class);
    }


    public Boolean toggleSmartAlerts(Integer userId) {

        User user = userRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }

        user.setSmartAlertsEnabled(!user.getSmartAlertsEnabled());

        userRepository.save(user);

        return user.getSmartAlertsEnabled();
    }
}
