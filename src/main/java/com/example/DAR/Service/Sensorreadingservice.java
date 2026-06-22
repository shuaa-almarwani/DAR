package com.example.DAR.Service;

import com.example.DAR.Api.ApiException;
import com.example.DAR.DTO.In.SensorReadingDtoIn;
import com.example.DAR.DTO.Out.SensorReportDtoOut;
import com.example.DAR.Model.Home;
import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.SensorReading;
import com.example.DAR.Repository.HomeRepository;
import com.example.DAR.Repository.SensorReadingRepository;
import com.example.DAR.Repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Sensorreadingservice {
    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;
    private final HomeRepository homeRepository;
    private final AiService aiService;
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

    // Builds a home-level sensor report from recent readings.
    public SensorReportDtoOut getSensorReport(Integer homeId) {
        Home home = homeRepository.findHomeById(homeId);
        if (home == null) throw new ApiException("Home not found");

        List<Sensor> sensors = sensorRepository.findSensorByHome_Id(homeId);

        List<SensorReportDtoOut.SensorStatDtoOut> stats = new ArrayList<>();
        StringBuilder dataForAi = new StringBuilder();

        for (Sensor sensor : sensors) {
            SensorReading latest = sensorReadingRepository.findTopBySensorsIdOrderByReadingDateDesc(sensor.getId());
            Double avg = sensorReadingRepository.findAvgBySensorId(sensor.getId());
            Double min = sensorReadingRepository.findMinBySensorId(sensor.getId());
            Double max = sensorReadingRepository.findMaxBySensorId(sensor.getId());
            Integer count = sensorReadingRepository.countBySensorId(sensor.getId());

            String unit = latest != null ? latest.getUnit() : "-";
            Double latestVal = latest != null ? latest.getReadingValue() : null;

            stats.add(new SensorReportDtoOut.SensorStatDtoOut(
                    sensor.getId(), sensor.getType(), sensor.getLocation(),
                    sensor.getIsActive(), unit,
                    avg != null ? Math.round(avg * 100.0) / 100.0 : null,
                    min, max, count, latestVal
            ));

            dataForAi.append("Sensor: ").append(sensor.getType())
                    .append(", Location: ").append(sensor.getLocation())
                    .append(", Active: ").append(sensor.getIsActive())
                    .append(", Readings: ").append(count)
                    .append(", Avg: ").append(avg != null ? String.format("%.2f", avg) : "N/A")
                    .append(", Min: ").append(min != null ? min : "N/A")
                    .append(", Max: ").append(max != null ? max : "N/A")
                    .append(", Latest: ").append(latestVal != null ? latestVal : "N/A")
                    .append(" ").append(unit).append("\n");
        }

        long activeSensors = sensors.stream().filter(s -> Boolean.TRUE.equals(s.getIsActive())).count();
        String aiSummary = sensors.isEmpty() ? "No sensors found for this home." :
                aiService.generateSensorReportSummary(home.getAddress(), dataForAi.toString());

        return new SensorReportDtoOut(home.getAddress(), sensors.size(), (int) activeSensors, stats, aiSummary);
    }

    // DELETE
    public void deleteReading(Integer id) {
        if (!sensorReadingRepository.existsById(id)) {
            throw new RuntimeException("Reading not found " );
        }
        sensorReadingRepository.deleteById(id);
    }
}
