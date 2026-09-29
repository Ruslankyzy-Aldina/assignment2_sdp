package org.example.factories;
import org.example.families.road.*;
import org.example.products.*;
import org.example.creators.*;
public final class RoadFactory implements TransportFactory<Road> {
    public Planner<Road> createPlanner() { return new RoadPlanner(); }
    public Labeler<Road> createLabeler() { return new RoadLabeler(); }
    public IntakeScanner<Road> createIntakeScanner() { return new RoadIntakeScanner(); }
    public DispatchCreator<Road> createDispatchCreator() { return new RoadDispatchCreator(); }
}
