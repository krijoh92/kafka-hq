package com.example.kafka;

import com.example.store.Area;

import java.time.Instant;

public record AreaEvent(Area Area, long Quantity, Instant TimeStamp) {
}

