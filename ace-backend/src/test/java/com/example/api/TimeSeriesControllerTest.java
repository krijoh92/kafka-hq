package com.example.api;

import com.example.service.MeasurementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TimeSeriesController.class)
class TimeSeriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    MeasurementService service;

    @Test
    void getTimeSeries() throws Exception {
        mockMvc.perform(get("/api/timeseries?area=NO1&start=2025-01-01T00:00:00Z&end=2025-01-01T02:00:00Z"))
                .andExpect(status().isOk());
    }
}
