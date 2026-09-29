package org.example.families.road;
import org.example.model.*;
import org.example.products.Planner;
public final class RoadPlanner implements Planner<Road> {
    public int capacityKg() { return 500; }
    public Plan<Road> plan(Shipment shipment) {
        if (shipment.kilograms() > capacityKg())
            throw new IllegalArgumentException("Shipment exceeds road capacity");
        return new Plan<>(shipment, 40 + 2 * shipment.kilograms(), 3, "Padded suspension crate");
    }
}
