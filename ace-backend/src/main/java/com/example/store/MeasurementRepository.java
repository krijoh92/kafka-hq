package com.example.store;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findByAreaAndTimestampBetweenOrderByTimestamp(Area area, Instant start, Instant end);
}
