package com.example.api;

import com.example.store.Area;

public record TimeSeriesResponse(Area area, java.util.List<SeriesPointResponse> series) {
}
