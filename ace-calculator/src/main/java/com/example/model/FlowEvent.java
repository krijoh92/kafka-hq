package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

public class FlowEvent {
    private final String InArea;
    private final String OutArea;
    private final long Quantity;
    private final Instant TimeStamp;

    @JsonCreator
    public FlowEvent(
            @JsonProperty("InArea") String inArea,
            @JsonProperty("OutArea") String outArea,
            @JsonProperty("Quantity") long quantity,
            @JsonProperty("TimeStamp") Instant timeStamp
    ) {
        this.InArea = inArea;
        this.OutArea = outArea;
        this.Quantity = quantity;
        this.TimeStamp = timeStamp;
    }

    @JsonProperty("InArea")
    public String getInArea() {
        return InArea;
    }

    @JsonProperty("OutArea")
    public String getOutArea() {
        return OutArea;
    }

    @JsonProperty("Quantity")
    public long getQuantity() {
        return Quantity;
    }

    @JsonProperty("TimeStamp")
    public Instant getTimeStamp() {
        return TimeStamp;
    }

    @Override
    public String toString() {
        return "FlowEvent{" +
                "InArea='" + InArea + '\'' +
                ", OutArea='" + OutArea + '\'' +
                ", Quantity=" + Quantity +
                ", TimeStamp=" + TimeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlowEvent that = (FlowEvent) o;
        return Quantity == that.Quantity &&
                Objects.equals(InArea, that.InArea) &&
                Objects.equals(OutArea, that.OutArea) &&
                Objects.equals(TimeStamp, that.TimeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(InArea, OutArea, Quantity, TimeStamp);
    }
}
