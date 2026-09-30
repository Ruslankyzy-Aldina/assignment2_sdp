package org.example.factories;
import org.example.model.Family;
import org.example.products.*;
import org.example.creators.DispatchCreator;

public interface TransportFactory<F extends Family> {
    Planner<F> createPlanner();
    Labeler<F> createLabeler();
    IntakeScanner<F> createIntakeScanner();
    DispatchCreator<F> createDispatchCreator();
}
