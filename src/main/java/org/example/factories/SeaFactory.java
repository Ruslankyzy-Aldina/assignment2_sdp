package org.example.factories;
import org.example.families.sea.*;
import org.example.products.*;
import org.example.creators.*;
public final class SeaFactory implements TransportFactory<Sea> {
    public Planner<Sea> createPlanner() { return new SeaPlanner(); }
    public Labeler<Sea> createLabeler() { return new SeaLabeler(); }
    public IntakeScanner<Sea> createIntakeScanner() { return new SeaIntakeScanner(); }
    public DispatchCreator<Sea> createDispatchCreator() { return new SeaDispatchCreator(); }
}
