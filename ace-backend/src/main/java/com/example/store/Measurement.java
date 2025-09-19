package com.example.store;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "measurements", indexes = {
        @Index(name = "idx_area_ts", columnList = "area, timestamp")
})
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private Area area;

    @Column(nullable = false)
    private long quantity;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    public Measurement() {
    }

    public Measurement(Area area, long quantity, Instant timestamp) {
        this.area = area;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
