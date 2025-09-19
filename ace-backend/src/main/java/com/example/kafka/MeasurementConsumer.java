package com.example.kafka;

import com.example.store.Measurement;
import com.example.service.MeasurementService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class MeasurementConsumer {

    private static final Logger log = LoggerFactory.getLogger(MeasurementConsumer.class);
    private final ObjectMapper objectMapper;
    private final MeasurementService service;

    public MeasurementConsumer(ObjectMapper objectMapper, MeasurementService service) {
        this.objectMapper = objectMapper;
        this.service = service;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        try {
            List<AreaEvent> points = objectMapper.readValue(message, new TypeReference<List<AreaEvent>>() {
            });
            List<Measurement> entities = points.stream()
                    .filter(p -> p.Area() != null)
                    .map(p -> new Measurement(p.Area(), p.Quantity(), p.TimeStamp()))
                    .toList();
            service.saveAll(entities);
            log.info("Stored {} measurements", entities.size());
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
        }
    }
}
