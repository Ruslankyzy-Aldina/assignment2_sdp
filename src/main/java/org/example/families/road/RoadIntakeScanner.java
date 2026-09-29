package org.example.families.road;
import org.example.model.*;
import org.example.products.IntakeScanner;
public final class RoadIntakeScanner implements IntakeScanner<Road> {
    public boolean safe(Label<Road> label, Reading reading) {
        return label.payload().equals(new RoadLabeler().encode(label.plan()).payload())
            && reading.shockG() <= 3 && reading.pressureKpa() >= 80
            && reading.humidityPercent() <= 65;
    }
}
