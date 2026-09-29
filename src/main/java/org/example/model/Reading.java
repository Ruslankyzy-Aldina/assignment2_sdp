package org.example.model;
/** Measurements on arrival; units are deliberately explicit. */
public record Reading(int shockG, int pressureKpa, int humidityPercent) {
    public Reading {
        if (shockG < 0 || pressureKpa <= 0 || humidityPercent < 0 || humidityPercent > 100)
            throw new IllegalArgumentException("Invalid sensor measurement");
    }
}
