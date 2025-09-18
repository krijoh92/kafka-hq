package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Objects;

public class AreaEvent {
    private final String Area;
    private final long Quantity;
    private final Instant TimeStamp;

    @JsonCreator
    public AreaEvent(
            @JsonProperty("Area") String area,
            @JsonProperty("Quantity") long quantity,
            @JsonProperty("TimeStamp") Instant timeStamp
    ) {
        this.Area = area;
        this.Quantity = quantity;
        this.TimeStamp = timeStamp;
    }

    @JsonProperty("Area")
    public String getArea() {
        return Area;
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
        return "AreaEvent{" +
                "Area='" + Area + '\'' +
                ", Quantity=" + Quantity +
                ", TimeStamp=" + TimeStamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaEvent that = (AreaEvent) o;
        return Quantity == that.Quantity &&
                Objects.equals(Area, that.Area) &&
                Objects.equals(TimeStamp, that.TimeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Area, Quantity, TimeStamp);
    }
}
