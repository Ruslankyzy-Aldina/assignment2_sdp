package org.example.model;
public record Reading(int shockG, int pressureKpa, int humidityPercent) {
    public Reading {
        if (shockG < 0 || pressureKpa <= 0 || humidityPercent < 0 || humidityPercent > 100)
            throw new IllegalArgumentException("Invalid sensor measurement");
    }
}
