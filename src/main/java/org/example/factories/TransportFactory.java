package org.example.factories;
import org.example.model.Family;
import org.example.products.*;
import org.example.creators.DispatchCreator;

/** Every return type shares F, including the Factory Method booking workflow. */
public interface TransportFactory<F extends Family> {
    Planner<F> createPlanner();
    Labeler<F> createLabeler();
    IntakeScanner<F> createIntakeScanner();
    DispatchCreator<F> createDispatchCreator();
}
