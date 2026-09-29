package org.example.creators;
import org.example.families.air.*;
import org.example.products.Planner;
public final class AirDispatchCreator extends DispatchCreator<Air> {
    @Override protected Planner<Air> createPlanner() { return new AirPlanner(); }
}
