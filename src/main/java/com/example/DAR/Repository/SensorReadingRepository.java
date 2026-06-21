package com.example.DAR.Repository;

import com.example.DAR.Model.Sensor;
import com.example.DAR.Model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading,Integer> {
    SensorReading findSensorReadingById(Integer id);
    List<SensorReading> findSensorReadingBySensors(Sensor sensors);
    SensorReading findTopBySensorsIdOrderByReadingDateDesc(Integer sensorsId);

    @Query("SELECT AVG(r.readingValue) FROM SensorReading r WHERE r.sensors.id = :sensorId")
    Double findAvgBySensorId(@Param("sensorId") Integer sensorId);

    @Query("SELECT MIN(r.readingValue) FROM SensorReading r WHERE r.sensors.id = :sensorId")
    Double findMinBySensorId(@Param("sensorId") Integer sensorId);

    @Query("SELECT MAX(r.readingValue) FROM SensorReading r WHERE r.sensors.id = :sensorId")
    Double findMaxBySensorId(@Param("sensorId") Integer sensorId);

    @Query("SELECT COUNT(r) FROM SensorReading r WHERE r.sensors.id = :sensorId")
    Integer countBySensorId(@Param("sensorId") Integer sensorId);

    List<SensorReading> findBySensorsIdAndReadingDateBetween(Integer sensorId, LocalDateTime from, LocalDateTime to);
}
