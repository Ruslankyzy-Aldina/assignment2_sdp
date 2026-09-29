package org.example.creators;

import org.example.model.*;
import org.example.products.Planner;

/** Factory Method: subclasses supply a planner for this shared booking workflow. */
public abstract class DispatchCreator<F extends Family> {
    protected abstract Planner<F> createPlanner();

    public final Plan<F> prepare(Shipment shipment, int budget, int deadlineDays) {
        if (budget < 0 || deadlineDays <= 0)
            throw new IllegalArgumentException("Budget must be nonnegative and deadline positive");
        Planner<F> planner = createPlanner();
        if (shipment.kilograms() > planner.capacityKg())
            throw new IllegalArgumentException("Crate exceeds transport capacity");
        Plan<F> plan = planner.plan(shipment);
        if (plan.cost() > budget) throw new IllegalArgumentException("Budget exceeded");
        if (plan.days() > deadlineDays) throw new IllegalArgumentException("Exhibition deadline missed");
        return plan;
    }
}
