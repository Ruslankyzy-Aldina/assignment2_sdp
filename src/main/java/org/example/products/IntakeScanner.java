package org.example.products;
import org.example.model.*;
public interface IntakeScanner<F extends Family> {
    boolean safe(Label<F> label, Reading reading);
}
