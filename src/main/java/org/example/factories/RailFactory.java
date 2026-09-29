package org.example.factories;
import org.example.families.rail.*;
import org.example.products.*;
import org.example.creators.*;
public final class RailFactory implements TransportFactory<Rail> {
    public Planner<Rail> createPlanner() { return new RailPlanner(); }
    public Labeler<Rail> createLabeler() { return new RailLabeler(); }
    public IntakeScanner<Rail> createIntakeScanner() { return new RailIntakeScanner(); }
    public DispatchCreator<Rail> createDispatchCreator() { return new RailDispatchCreator(); }
}
