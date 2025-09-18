package com.example;

import com.example.model.AreaEvent;
import com.example.model.FlowEvent;
import com.example.serde.SerdesUtil;
import com.example.streams.TopologyConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    void integrationTestWithoutDockerBecauseWhyWouldDockerBeAllowedOnAWorkComputer() {
        StreamsBuilder builder = new StreamsBuilder();
        Topology topology = TopologyConfig.buildTopology(builder, "A", "B");

        Serde<String> keySerde = Serdes.String();
        var inValueSerde = SerdesUtil.flowEventListSerde();
        var outValueSerde = SerdesUtil.areaEventListSerde();

        Properties props = new Properties();
        props.put("application.id", "topology-driver-test");
        props.put("bootstrap.servers", "dummy:9092");

        try (TopologyTestDriver driver = new TopologyTestDriver(topology, props)) {
            TestInputTopic<String, List<FlowEvent>> input =
                    driver.createInputTopic("A", new StringSerializer(), inValueSerde.serializer());
            TestOutputTopic<String, List<AreaEvent>> output =
                    driver.createOutputTopic("B", new StringDeserializer(), outValueSerde.deserializer());

            Instant ts = Instant.parse("2025-01-01T00:00:00Z");
            List<FlowEvent> flows = List.of(
                    new FlowEvent("NO1", "NO2", 300, ts),
                    new FlowEvent("NO3", "NO1", 100, ts)
            );
            input.pipeInput("key", flows, ts);

            assertFalse(output.isEmpty(), "Expected at least one output record");

            List<AreaEvent> deltas = new ArrayList<>();
            while (!output.isEmpty()) {
                var rec = output.readRecord();
                if (rec.value() != null) deltas.addAll(rec.value());
            }

            assertEquals(3, deltas.size(), "Expected three area deltas (NO1, NO2, NO3)");
            assertEquals(1, deltas.stream().map(AreaEvent::getTimeStamp).distinct().count());

            Map<String, Long> byArea = deltas.stream()
                    .collect(Collectors.toMap(AreaEvent::getArea, AreaEvent::getQuantity));

            assertEquals(200L, byArea.get("NO1"));
            assertEquals(-300L, byArea.get("NO2"));
            assertEquals(100L, byArea.get("NO3"));
        }
    }
}
