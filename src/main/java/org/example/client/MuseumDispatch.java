package org.example.client;

import org.example.factories.TransportFactory;
import org.example.creators.DispatchCreator;
import org.example.products.*;
import org.example.model.*;

/** The business client knows only abstractions and the family type parameter. */
public final class MuseumDispatch<F extends Family> {
    private final Planner<F> planner;
    private final Labeler<F> labeler;
    private final IntakeScanner<F> scanner;
    private final DispatchCreator<F> creator;

    public MuseumDispatch(TransportFactory<F> factory) {
        planner = factory.createPlanner();
        labeler = factory.createLabeler();
        scanner = factory.createIntakeScanner();
        creator = factory.createDispatchCreator();
    }

    /** Check capacity, budget and deadline before issuing a manifest. */
    public Label<F> book(Shipment shipment, int budget, int deadlineDays) {
        return labeler.encode(creator.prepare(shipment, budget, deadlineDays));
    }

    /** Damaged crates cannot travel further; a new destination needs a new manifest. */
    public Label<F> reroute(Label<F> current, Reading reading, String destination,
                            int budget, int deadlineDays) {
        if (!scanner.safe(current, reading))
            throw new IllegalStateException("Unsafe crate must be quarantined before rerouting");
        return book(current.plan().shipment().redirectedTo(destination), budget, deadlineDays);
    }

    /** Recompute the expected plan and manifest, then inspect the arrival sensors. */
    public String receive(Label<F> label, Reading reading) {
        Plan<F> expected = planner.plan(label.plan().shipment());
        boolean authentic = expected.equals(label.plan())
            && labeler.encode(expected).payload().equals(label.payload());
        return authentic && scanner.safe(label, reading)
            ? "ACCEPTED " + expected.shipment().artifactId()
            : "QUARANTINE " + expected.shipment().artifactId();
    }
}
