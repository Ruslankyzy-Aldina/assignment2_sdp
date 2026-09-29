package org.example.families.road;
import org.example.model.*;
import org.example.products.Labeler;
public final class RoadLabeler implements Labeler<Road> {
    public Label<Road> encode(Plan<Road> plan) {
        return new Label<>(plan, "ROAD|" + plan.shipment().artifactId() + "|"
            + plan.shipment().destination() + "|" + plan.cost() + "|" + plan.handling());
    }
}
