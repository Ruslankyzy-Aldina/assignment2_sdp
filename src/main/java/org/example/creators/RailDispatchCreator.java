package org.example.creators;
import org.example.families.rail.*;
import org.example.products.Planner;
public final class RailDispatchCreator extends DispatchCreator<Rail> {
    @Override protected Planner<Rail> createPlanner() { return new RailPlanner(); }
}
