package com.example.api;

import com.example.store.Area;
import com.example.store.Measurement;
import com.example.service.MeasurementService;
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
    public ResponseEntity<?> getSeries(
            @RequestParam(name = "area") Area area,
            @RequestParam(name = "start") Instant start,
            @RequestParam(name = "end") Instant end
    ) {
        if (end.isBefore(start)) {
            return ResponseEntity.badRequest().body("end must be >= start");
        }
        List<Measurement> data = service.getSeries(area, start, end);
        List<SeriesPointResponse> points = data.stream()
                .map(m -> new SeriesPointResponse(m.getTimestamp().toString(), m.getQuantity()))
                .toList();
        return ResponseEntity.ok(new TimeSeriesResponse(area, points));
    }
}
