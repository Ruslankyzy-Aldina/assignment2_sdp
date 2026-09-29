package org.example;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import javax.tools.*;
import java.util.List;
import org.example.client.MuseumDispatch;
import org.example.config.FactoryRegistry;
import org.example.creators.*;
import org.example.factories.*;
import org.example.families.road.*;
import org.example.families.air.*;
import org.example.families.sea.*;
import org.example.model.*;
import org.example.products.*;

/** Dependency-free test runner: every case throws on failure; assertions need no -ea. */
public final class AssignmentTests {
    private static int passed;
    private static final Shipment CRATE = new Shipment("ART-101", 20, "SITE-A");
    private static final Reading SAFE = new Reading(1, 101, 40);
    private static final MuseumDispatch<Road> ROAD = new MuseumDispatch<>(new RoadFactory());

    public static void main(String[] args) throws Exception {
        test("Road factory creates the three correct products", () -> {
            var f = new RoadFactory();
            check(f.createPlanner() instanceof RoadPlanner);
            check(f.createLabeler() instanceof RoadLabeler);
            check(f.createIntakeScanner() instanceof RoadIntakeScanner);
        });
        test("Air factory creates the three correct products", () -> {
            var f = new AirFactory();
            check(f.createPlanner() instanceof AirPlanner);
            check(f.createLabeler() instanceof AirLabeler);
            check(f.createIntakeScanner() instanceof AirIntakeScanner);
        });
        test("Sea factory creates the three correct products", () -> {
            var f = new SeaFactory();
            check(f.createPlanner() instanceof SeaPlanner);
            check(f.createLabeler() instanceof SeaLabeler);
            check(f.createIntakeScanner() instanceof SeaIntakeScanner);
        });
        test("Road kit collaborates", () -> compatible(new RoadFactory()));
        test("Air kit collaborates", () -> compatible(new AirFactory()));
        test("Sea kit collaborates", () -> compatible(new SeaFactory()));
        test("Road runtime selection", () -> check(FactoryRegistry.select("road") instanceof RoadFactory));
        test("Air runtime selection", () -> check(FactoryRegistry.select("air") instanceof AirFactory));
        test("Sea runtime selection", () -> check(FactoryRegistry.select("sea") instanceof SeaFactory));
        test("Selection normalizes case and whitespace", () -> check(FactoryRegistry.select(" AIR ") instanceof AirFactory));
        test("Unknown selection rejected", () -> expect(IllegalArgumentException.class, () -> FactoryRegistry.select("space")));
        test("Null selection rejected", () -> expect(IllegalArgumentException.class, () -> FactoryRegistry.select(null)));
        test("Booking includes price destination and handling", () -> {
            var label = ROAD.book(CRATE, 80, 3);
            check(label.plan().cost() == 80 && label.plan().days() == 3);
            check(label.payload().equals("ROAD|ART-101|SITE-A|80|Padded suspension crate"));
        });
        test("Reroute reissues manifest and preserves original", () -> {
            var old = ROAD.book(CRATE, 1000, 20);
            var redirected = ROAD.reroute(old, SAFE, "SITE-B", 1000, 20);
            check(redirected.plan().shipment().destination().equals("SITE-B"));
            check(old.plan().shipment().destination().equals("SITE-A"));
            check(!old.payload().equals(redirected.payload()));
        });
        test("Safe arrival accepted", () -> check(ROAD.receive(ROAD.book(CRATE, 1000, 20), SAFE).startsWith("ACCEPTED")));
        test("Shock damage quarantined", () -> check(ROAD.receive(ROAD.book(CRATE, 1000, 20), new Reading(4, 101, 40)).startsWith("QUARANTINE")));
        test("Unsafe reroute rejected", () -> expect(IllegalStateException.class, () -> ROAD.reroute(ROAD.book(CRATE, 1000, 20), new Reading(8, 101, 40), "SITE-B", 1000, 20)));
        test("Altered label quarantined", () -> {
            var good = ROAD.book(CRATE, 1000, 20);
            check(ROAD.receive(new Label<>(good.plan(), "FORGED"), SAFE).startsWith("QUARANTINE"));
        });
        test("Altered plan quarantined even with matching label", () -> {
            var plan = new Plan<Road>(CRATE, 1, 3, "Padded suspension crate");
            check(ROAD.receive(new RoadLabeler().encode(plan), SAFE).startsWith("QUARANTINE"));
        });
        test("Overweight rejected", () -> expect(IllegalArgumentException.class, () -> ROAD.book(new Shipment("ART-101", 501, "SITE-A"), 10000, 20)));
        test("Budget enforced", () -> expect(IllegalArgumentException.class, () -> ROAD.book(CRATE, 79, 20)));
        test("Deadline enforced", () -> expect(IllegalArgumentException.class, () -> ROAD.book(CRATE, 1000, 2)));
        test("Zero deadline rejected", () -> expect(IllegalArgumentException.class, () -> ROAD.book(CRATE, 1000, 0)));
        test("Negative budget rejected", () -> expect(IllegalArgumentException.class, () -> ROAD.book(CRATE, -1, 20)));
        test("Zero weight rejected", () -> expect(IllegalArgumentException.class, () -> new Shipment("ART-101", 0, "SITE-A")));
        test("Invalid measurement rejected", () -> expect(IllegalArgumentException.class, () -> new Reading(1, 101, 101)));
        test("Air pressure rules differ from road", () -> {
            var r = new Reading(1, 85, 40);
            var air = new AirFactory();
            check(!air.createIntakeScanner().safe(air.createLabeler().encode(air.createPlanner().plan(CRATE)), r));
            check(new RoadIntakeScanner().safe(ROAD.book(CRATE, 1000, 20), r));
        });
        test("Sea humidity rule is stricter", () -> {
            var sea = new SeaFactory();
            check(!sea.createIntakeScanner().safe(sea.createLabeler().encode(sea.createPlanner().plan(CRATE)), new Reading(1, 101, 46)));
        });
        test("Factory Method subclasses choose different behavior", () -> {
            check(new RoadDispatchCreator().prepare(CRATE, 1000, 20).cost() == 80);
            check(new AirDispatchCreator().prepare(CRATE, 1000, 20).cost() == 360);
            check(new SeaDispatchCreator().prepare(CRATE, 1000, 20).cost() == 120);
        });
        test("Business client accepts a new test-only abstract factory", AssignmentTests::abstractionTest);
        test("Compiler rejects a mixed family label", AssignmentTests::rejectMixedFamily);
        System.out.println("PASS: " + passed + " tests");
    }

    private static <F extends Family> void compatible(TransportFactory<F> factory) {
        var label = factory.createLabeler().encode(factory.createPlanner().plan(CRATE));
        check(factory.createIntakeScanner().safe(label, SAFE));
    }

    private static final class TestFamily implements Family {}
    private static void abstractionTest() {
        int[] calls = new int[3];
        Planner<TestFamily> planner = new Planner<>() {
            public int capacityKg() { return 50; }
            public Plan<TestFamily> plan(Shipment shipment) {
                calls[0]++;
                return new Plan<>(shipment, 7, 2, "Test handling");
            }
        };
        TransportFactory<TestFamily> fake = new TransportFactory<>() {
            public Planner<TestFamily> createPlanner() { return planner; }
            public Labeler<TestFamily> createLabeler() {
                return plan -> { calls[1]++; return new Label<>(plan, plan.shipment().destination()); };
            }
            public IntakeScanner<TestFamily> createIntakeScanner() {
                return (label, reading) -> { calls[2]++; return true; };
            }
            public DispatchCreator<TestFamily> createDispatchCreator() {
                return new DispatchCreator<>() {
                    protected Planner<TestFamily> createPlanner() { return planner; }
                };
            }
        };
        var client = new MuseumDispatch<>(fake);
        var first = client.book(CRATE, 7, 2);
        var next = client.reroute(first, SAFE, "SITE-B", 7, 2);
        check(client.receive(next, SAFE).equals("ACCEPTED ART-101"));
        check(calls[0] == 3 && calls[1] == 3 && calls[2] == 2);
    }

    private static void rejectMixedFamily() throws IOException {
        var directory = Files.createTempDirectory(Path.of("target"), "compatibility-");
        var source = directory.resolve("MixedFamily.java");
        Files.writeString(source, """
            import org.example.families.road.*;
            import org.example.families.air.*;
            import org.example.model.*;
            class MixedFamily {
                void invalid() {
                    var roadPlan = new RoadPlanner().plan(new Shipment("ART-101",20,"SITE-A"));
                    new AirLabeler().encode(roadPlan);
                }
            }
            """, StandardCharsets.UTF_8);
        var compiler = ToolProvider.getSystemJavaCompiler();
        check(compiler != null);
        var diagnostics = new DiagnosticCollector<JavaFileObject>();
        try (var manager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            boolean success = compiler.getTask(null, manager, diagnostics,
                List.of("-classpath", System.getProperty("java.class.path"), "-d", directory.toString()),
                null, manager.getJavaFileObjects(source.toFile())).call();
            check(!success);
            check(diagnostics.getDiagnostics().stream().anyMatch(d -> d.getKind() == Diagnostic.Kind.ERROR
                && d.getMessage(java.util.Locale.ROOT).contains("Plan<org.example.families.road.Road>")
                && d.getMessage(java.util.Locale.ROOT).contains("Plan<org.example.families.air.Air>")));
        } finally { Files.deleteIfExists(source); Files.deleteIfExists(directory); }
    }

    @FunctionalInterface private interface Case { void run() throws Exception; }
    private static void test(String name, Case action) throws Exception {
        action.run(); passed++; System.out.println("PASS " + name);
    }
    private static void check(boolean value) {
        if (!value) throw new AssertionError("Condition failed");
    }
    private static void expect(Class<? extends Throwable> type, Case action) throws Exception {
        try { action.run(); }
        catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError("Unexpected exception", error);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }
}
