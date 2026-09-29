package org.example.families.rail;
import org.example.model.*;
import org.example.products.Planner;
public final class RailPlanner implements Planner<Rail> {
    public int capacityKg() { return 1000; }
    public Plan<Rail> plan(Shipment shipment) {
        if (shipment.kilograms() > capacityKg())
            throw new IllegalArgumentException("Shipment exceeds rail capacity");
        return new Plan<>(shipment, 70 + 1 * shipment.kilograms(), 5, "Vibration isolated rail crate");
    }
}
