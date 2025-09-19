package com.example.service;

import com.example.store.Area;
import com.example.store.Measurement;
import com.example.store.MeasurementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void saveAll(List<Measurement> items) {
        repository.saveAll(items);
    }

    @Transactional(readOnly = true)
    public List<Measurement> getSeries(Area area, Instant start, Instant end) {
        return repository.findByAreaAndTimestampBetweenOrderByTimestamp(area, start, end);
    }
}
