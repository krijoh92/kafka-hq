package com.example.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.List;
import com.example.model.FlowEvent;
import com.example.model.AreaEvent;

public final class SerdesUtil {
    private SerdesUtil() { }

    public static JsonSerde<List<FlowEvent>> flowEventListSerde() {
        JsonSerde<List<FlowEvent>> s = new JsonSerde<>(new TypeReference<List<FlowEvent>>() {});
        // Ensure we use the target type instead of type headers
        s.deserializer().ignoreTypeHeaders();
        s.deserializer().addTrustedPackages("*");
        s.serializer().setAddTypeInfo(false);
        return s;
    }

    public static JsonSerde<List<AreaEvent>> areaEventListSerde() {
        JsonSerde<List<AreaEvent>> s = new JsonSerde<>(new TypeReference<List<AreaEvent>>() {});
        s.deserializer().ignoreTypeHeaders();
        s.deserializer().addTrustedPackages("*");
        s.serializer().setAddTypeInfo(false);
        return s;
    }
}
