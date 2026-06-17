package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.SensorReadingDtoIn;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.SensorReading;
import com.example.DAR.Repository.SensorReadingRepository;
import com.example.DAR.Repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Sensorreadingservice {
    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final ModelMapper modelMapper;

    // CREATE
    public void addReading(Integer sensorId, SensorReadingDtoIn dto) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found  " ));

        SensorReading reading = modelMapper.map(dto, SensorReading.class);
        reading.setSensors(sensor);
        reading.setReadingDate(LocalDateTime.now()); // تلقائي

         sensorReadingRepository.save(reading);
    }

    // GET ALL by Sensor
    public List<SensorReading> getAllReadingsBySensor(Integer sensorId) {
        Sensor sensor =sensorRepository.findSensorById(sensorId);
        if (sensor == null) throw new ApiException("sensor not found");

        return sensorReadingRepository.findSensorReadingBySensors(sensor);
    }

    // GET ONE
    public SensorReading getReadingById(Integer id) {
        return sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reading not found " ));
    }

    // GET Latest Reading
    public SensorReading getLatestReading(Integer sensorId) {
        return sensorReadingRepository.findTopBySensorsIdOrderByReadingDateDesc(sensorId);
    }

    // DELETE
    public void deleteReading(Integer id) {
        if (!sensorReadingRepository.existsById(id)) {
            throw new RuntimeException("Reading not found " );
        }
        sensorReadingRepository.deleteById(id);
    }
}
