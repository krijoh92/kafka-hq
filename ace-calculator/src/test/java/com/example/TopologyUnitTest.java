package com.example;

import com.example.model.AreaEvent;
import com.example.model.FlowEvent;
import com.example.streams.TopologyConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TopologyUnitTest {

    private final static Instant INSTANT = Instant.parse("2025-01-01T00:00:00Z");

    @Test
    void singleFlowToAreaEvents() {
        List<FlowEvent> input = List.of(new FlowEvent("NO1", "NO2", 1, INSTANT));
        List<AreaEvent> out = TopologyConfig.toAreaEvents(input);

        assertEquals(2, out.size());
        assertThat(out)
                .contains(new AreaEvent("NO1", 1, INSTANT))
                .contains(new AreaEvent("NO2", -1, INSTANT));
    }


    @Test
    void multipleFlowToAreaEvents() {
        FlowEvent flowEvent1 = new FlowEvent("NO1", "NO2", 1, INSTANT);
        FlowEvent flowEvent2 = new FlowEvent("NO1", "NO3", 2, INSTANT);

        List<FlowEvent> input = List.of(flowEvent1, flowEvent2);
        List<AreaEvent> out = TopologyConfig.toAreaEvents(input);

        assertEquals(3, out.size());
        assertThat(out)
                .contains(new AreaEvent("NO1", 3, INSTANT))
                .contains(new AreaEvent("NO2", -1, INSTANT))
                .contains(new AreaEvent("NO3", -2, INSTANT));
    }
}
