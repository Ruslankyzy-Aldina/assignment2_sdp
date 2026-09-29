package org.example.products;
import org.example.model.*;
public interface Planner<F extends Family> {
    int capacityKg();
    Plan<F> plan(Shipment shipment);
}
