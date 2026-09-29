package org.example.products;
import org.example.model.*;
public interface Labeler<F extends Family> {
    Label<F> encode(Plan<F> plan);
}
