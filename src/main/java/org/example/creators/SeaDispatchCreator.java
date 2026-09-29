package org.example.creators;
import org.example.families.sea.*;
import org.example.products.Planner;
public final class SeaDispatchCreator extends DispatchCreator<Sea> {
    @Override protected Planner<Sea> createPlanner() { return new SeaPlanner(); }
}
