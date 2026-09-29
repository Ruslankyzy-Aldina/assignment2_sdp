package org.example.families.rail;
import org.example.model.*;
import org.example.products.IntakeScanner;
public final class RailIntakeScanner implements IntakeScanner<Rail> {
    public boolean safe(Label<Rail> label, Reading reading) {
        return label.payload().equals(new RailLabeler().encode(label.plan()).payload())
            && reading.shockG() <= 2 && reading.pressureKpa() >= 80
            && reading.humidityPercent() <= 60;
    }
}
