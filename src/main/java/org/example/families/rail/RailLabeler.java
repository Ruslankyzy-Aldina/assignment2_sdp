package org.example.families.rail;
import org.example.model.*;
import org.example.products.Labeler;
public final class RailLabeler implements Labeler<Rail> {
    public Label<Rail> encode(Plan<Rail> plan) {
        return new Label<>(plan, "RAIL#" + plan.shipment().artifactId() + "|"
            + plan.shipment().destination() + "|" + plan.cost() + "|" + plan.handling());
    }
}
