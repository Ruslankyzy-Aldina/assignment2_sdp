package org.example.client;

import org.example.factories.TransportFactory;
import org.example.creators.DispatchCreator;
import org.example.products.*;
import org.example.model.*;

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

    public Label<F> book(Shipment shipment, int budget, int deadlineDays) {
        return labeler.encode(creator.prepare(shipment, budget, deadlineDays));
    }

    public Label<F> reroute(Label<F> current, Reading reading, String destination,
                            int budget, int deadlineDays) {
        if (!scanner.safe(current, reading))
            throw new IllegalStateException("Unsafe crate must be quarantined before rerouting");
        return book(current.plan().shipment().redirectedTo(destination), budget, deadlineDays);
    }

    public String receive(Label<F> label, Reading reading) {
        Plan<F> expected = planner.plan(label.plan().shipment());
        boolean authentic = expected.equals(label.plan())
            && labeler.encode(expected).payload().equals(label.payload());
        return authentic && scanner.safe(label, reading)
            ? "ACCEPTED " + expected.shipment().artifactId()
            : "QUARANTINE " + expected.shipment().artifactId();
    }
}
