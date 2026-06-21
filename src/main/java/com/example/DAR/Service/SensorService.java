package com.example.DAR.Service;


import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.SensorDtoIn;
import com.example.DAR.DTO.Out.SensorDtoOut;
import com.example.DAR.Enums.UserSubscriptionStatus;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.UserSubscription;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.SensorRepository;
import com.example.DAR.Repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final ModelMapper modelMapper;
    private final HomeRepository homeRepository ;
    private final UserSubscriptionRepository userSubscriptionRepository;

    public void addSensor(Integer homeId, SensorDtoIn dto) {

        Home home = homeRepository.findHomeById(homeId);
        if (home==null) {
            throw new ApiException("home not found");
        }
        UserSubscription subscription = userSubscriptionRepository.findUserSubscriptionByUserIdAndStatus(home.getUser().getId(), UserSubscriptionStatus.ACTIVE);
        if (subscription == null) {
            throw new ApiException("Active subscription not found");
        }
        if (sensorRepository.countSensorsByHomeUserId(home.getUser().getId()) >= subscription.getSubscriptionPlan().getMaxSensors()) {
            throw new ApiException("You have reached the maximum number of sensors for your subscription");
        }

        Sensor sensor = modelMapper.map(dto, Sensor.class);
        sensor.setHome(home);
        sensor.setIsActive(false);
        sensor.setLastPing(LocalDate.now());

         sensorRepository.save(sensor);
    }
    public void updateSensor(Integer id, SensorDtoIn dto) {
        Sensor existing = sensorRepository.findSensorById(id);
        modelMapper.map(dto, existing);
        existing.setLastPing(LocalDate.now());
         sensorRepository.save(existing);
    }

    // TOGGLE Sensor to ACTIVE
    public void toggleActive(Integer id) {
        Sensor sensor = sensorRepository.findSensorById(id);
        if (sensor==null) {
            throw new ApiException("sensor not found");
        }
        if (sensor.getIsActive()){
            throw new ApiException("sensor is active ");
        }
        sensor.setIsActive(true);
        sensor.setIsRunning(true);
         sensorRepository.save(sensor);
    }
    // TOGGLE Sensor to NOT ACTIVE
    public void toggleDeactivate(Integer id) {
        Sensor sensor = sensorRepository.findSensorById(id);
        if (sensor==null) {
            throw new ApiException("sensor not found");
        }
        if (!sensor.getIsActive()){
            throw new ApiException("sensor is not active ");
        }
        sensor.setIsRunning(false);
        sensor.setIsActive(false);
        sensorRepository.save(sensor);
    }

    // DELETE
    public void deleteSensor(Integer id) {
        if (!sensorRepository.existsById(id)) {
            throw new ApiException("Sensor not found");
        }
        sensorRepository.deleteById(id);

    }
    // GET ALL by Home
    public List<SensorDtoOut> getAllSensorsByHome(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home==null) {
            throw new ApiException("home not found");
        }
        List<Sensor> sensor = sensorRepository.findSensorByHome_Id(homeId);
        return sensor.stream().map(s -> modelMapper.map(s, SensorDtoOut.class)).toList();

    }

    // GET ONE
    public SensorDtoOut getSensorById(Integer id) {
        Sensor sensor = sensorRepository.findSensorById(id);
        if (sensor==null) {
            throw new ApiException("sensor not found");
        }
        return modelMapper.map(sensor, SensorDtoOut.class) ;
    }

    // GET Active Sensors
    public List<SensorDtoOut> getActiveSensors(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home==null) {
            throw new ApiException("home not found");
        }
        List<Sensor> sensor = sensorRepository.findByHomeIdAndIsActiveTrue(homeId);
        return sensor.stream().map(s -> modelMapper.map(s, SensorDtoOut.class)).toList();

    }
}
