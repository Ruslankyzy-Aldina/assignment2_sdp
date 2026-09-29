package org.example.families.sea;
import org.example.model.*;
import org.example.products.Planner;
public final class SeaPlanner implements Planner<Sea> {
    public int capacityKg() { return 2000; }
    public Plan<Sea> plan(Shipment shipment) {
        if (shipment.kilograms() > capacityKg())
            throw new IllegalArgumentException("Shipment exceeds sea capacity");
        return new Plan<>(shipment, 100 + 1 * shipment.kilograms(), 14, "Desiccant lined marine crate");
    }
}
