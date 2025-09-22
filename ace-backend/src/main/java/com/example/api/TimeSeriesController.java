package com.example.api;

import com.example.store.Area;
import com.example.store.Measurement;
import com.example.service.MeasurementService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/timeseries")
public class TimeSeriesController {

    private final MeasurementService service;

    public TimeSeriesController(MeasurementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<TimeSeriesResponse> getSeries(
            @RequestParam(name = "area") Area area,
            @Parameter(description = "Start time (ISO-8601 UTC)", example = "2024-09-20T00:00:00Z")
            @RequestParam(name = "start") Instant start,
            @Parameter(description = "End time (ISO-8601 UTC)", example = "2025-09-20T00:00:00Z")
            @RequestParam(name = "end") Instant end
    ) {
        if (end.isBefore(start)) {
            return ResponseEntity.badRequest().build();
        }
        List<Measurement> data = service.getSeries(area, start, end);
        List<SeriesPointResponse> points = data.stream()
                .map(m -> new SeriesPointResponse(m.getTimestamp().toString(), m.getQuantity()))
                .toList();
        return ResponseEntity.ok(new TimeSeriesResponse(area, points));
    }
}
