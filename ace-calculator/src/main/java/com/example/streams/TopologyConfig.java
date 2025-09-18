package com.example.streams;

import com.example.model.AreaEvent;
import com.example.model.FlowEvent;
import com.example.serde.SerdesUtil;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class TopologyConfig {

    @Value("${app.topic.input:A}")
    private String inputTopic;

    @Value("${app.topic.output:B}")
    private String outputTopic;

    @Bean
    public Topology topology(StreamsBuilder builder) {
        // Spring path uses injected topics
        return buildTopology(builder, inputTopic, outputTopic);
    }

    // === Pure function: callable from tests without Spring or reflection ===
    public static Topology buildTopology(StreamsBuilder builder, String inputTopic, String outputTopic) {
        final var inputSerde = SerdesUtil.flowEventListSerde();
        final var outputSerde = SerdesUtil.areaEventListSerde();

        KStream<String, List<FlowEvent>> input =
                builder.stream(inputTopic, Consumed.with(Serdes.String(), inputSerde));

        KStream<String, List<AreaEvent>> out = input.mapValues(TopologyConfig::toAreaEvents);

        out.to(outputTopic, Produced.with(Serdes.String(), outputSerde));
        return builder.build();
    }

    // unchanged
    public static List<AreaEvent> toAreaEvents(List<FlowEvent> flows) {
        if (flows == null || flows.isEmpty()) return List.of();

        Map<Instant, Map<String, Long>> tsToArea = new LinkedHashMap<>();
        for (FlowEvent f : flows) {
            Instant ts = f.getTimeStamp();
            tsToArea.computeIfAbsent(ts, t -> new HashMap<>());
            Map<String, Long> areaMap = tsToArea.get(ts);
            areaMap.merge(f.getInArea(), f.getQuantity(), Long::sum);
            areaMap.merge(f.getOutArea(), -f.getQuantity(), Long::sum);
        }

        if (tsToArea.size() == 1) {
            Map.Entry<Instant, Map<String, Long>> e = tsToArea.entrySet().iterator().next();
            Instant ts = e.getKey();
            return e.getValue().entrySet().stream()
                    .map(en -> new AreaEvent(en.getKey(), en.getValue(), ts))
                    .sorted(Comparator.comparing(AreaEvent::getArea))
                    .collect(Collectors.toList());
        } else {
            return tsToArea.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .flatMap(e -> e.getValue().entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(en -> new AreaEvent(en.getKey(), en.getValue(), e.getKey())))
                    .collect(Collectors.toList());
        }
    }
}