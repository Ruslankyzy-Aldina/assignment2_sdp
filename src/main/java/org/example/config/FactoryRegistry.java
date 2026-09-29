package org.example.config;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import org.example.factories.*;

/** Composition root: the only runtime selection table. */
public final class FactoryRegistry {
    private static final Map<String, Supplier<TransportFactory<?>>> FACTORIES = Map.of(
        "road", RoadFactory::new,
        "air", AirFactory::new,
        "sea", SeaFactory::new,
        "rail", RailFactory::new
    );
    private FactoryRegistry() {}
    public static TransportFactory<?> select(String name) {
        if (name == null) throw new IllegalArgumentException("Transport name is required");
        var supplier = FACTORIES.get(name.trim().toLowerCase(Locale.ROOT));
        if (supplier == null) throw new IllegalArgumentException("Unknown transport: " + name);
        return supplier.get();
    }
}
