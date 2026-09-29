package org.example.families.sea;
import org.example.model.*;
import org.example.products.Labeler;
public final class SeaLabeler implements Labeler<Sea> {
    public Label<Sea> encode(Plan<Sea> plan) {
        return new Label<>(plan, "SEA:" + plan.shipment().artifactId() + "|"
            + plan.shipment().destination() + "|" + plan.cost() + "|" + plan.handling());
    }
}
