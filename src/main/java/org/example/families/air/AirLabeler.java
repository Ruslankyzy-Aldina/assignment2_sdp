package org.example.families.air;
import org.example.model.*;
import org.example.products.Labeler;
public final class AirLabeler implements Labeler<Air> {
    public Label<Air> encode(Plan<Air> plan) {
        return new Label<>(plan, "AIR/" + plan.shipment().artifactId() + "|"
            + plan.shipment().destination() + "|" + plan.cost() + "|" + plan.handling());
    }
}
