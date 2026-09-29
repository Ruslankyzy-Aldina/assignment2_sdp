package org.example.families.sea;
import org.example.model.*;
import org.example.products.IntakeScanner;
public final class SeaIntakeScanner implements IntakeScanner<Sea> {
    public boolean safe(Label<Sea> label, Reading reading) {
        return label.payload().equals(new SeaLabeler().encode(label.plan()).payload())
            && reading.shockG() <= 4 && reading.pressureKpa() >= 75
            && reading.humidityPercent() <= 45;
    }
}
