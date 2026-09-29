package org.example.families.air;
import org.example.model.*;
import org.example.products.Planner;
public final class AirPlanner implements Planner<Air> {
    public int capacityKg() { return 100; }
    public Plan<Air> plan(Shipment shipment) {
        if (shipment.kilograms() > capacityKg())
            throw new IllegalArgumentException("Shipment exceeds air capacity");
        return new Plan<>(shipment, 200 + 8 * shipment.kilograms(), 1, "Pressure sealed flight crate");
    }
}
