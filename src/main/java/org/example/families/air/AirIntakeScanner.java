package org.example.families.air;
import org.example.model.*;
import org.example.products.IntakeScanner;
public final class AirIntakeScanner implements IntakeScanner<Air> {
    public boolean safe(Label<Air> label, Reading reading) {
        return label.payload().equals(new AirLabeler().encode(label.plan()).payload())
            && reading.shockG() <= 2 && reading.pressureKpa() >= 90
            && reading.humidityPercent() <= 55;
    }
}
