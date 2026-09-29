package org.example.factories;
import org.example.families.air.*;
import org.example.products.*;
import org.example.creators.*;
public final class AirFactory implements TransportFactory<Air> {
    public Planner<Air> createPlanner() { return new AirPlanner(); }
    public Labeler<Air> createLabeler() { return new AirLabeler(); }
    public IntakeScanner<Air> createIntakeScanner() { return new AirIntakeScanner(); }
    public DispatchCreator<Air> createDispatchCreator() { return new AirDispatchCreator(); }
}
