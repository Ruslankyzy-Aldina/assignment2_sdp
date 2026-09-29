package org.example.creators;
import org.example.families.road.*;
import org.example.products.Planner;
public final class RoadDispatchCreator extends DispatchCreator<Road> {
    @Override protected Planner<Road> createPlanner() { return new RoadPlanner(); }
}
