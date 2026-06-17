package com.example.DAR.Repository;

import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading,Integer> {
    SensorReading findSensorReadingById(Integer id);
    List<SensorReading> findSensorReadingBySensors(Sensor sensors);
    SensorReading findTopBySensorsIdOrderByReadingDateDesc(Integer sensorsId);
}
