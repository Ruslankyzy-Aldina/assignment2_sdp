# Part G — Fourth family extension

Rail was added after the original three families and their 31 tests were complete.

New production files:

- `src/main/java/org/example/families/rail/Rail.java` — family type token.
- `src/main/java/org/example/families/rail/RailPlanner.java` — 1000 kg capacity, cost 70 + kg, five days, vibration isolation.
- `src/main/java/org/example/families/rail/RailLabeler.java` — RAIL# manifest.
- `src/main/java/org/example/families/rail/RailIntakeScanner.java` — maximum shock 2 g, minimum pressure 80 kPa, maximum humidity 60%.
- `src/main/java/org/example/creators/RailDispatchCreator.java` — Factory Method override.
- `src/main/java/org/example/factories/RailFactory.java` — compatible product kit.

Existing files modified in the extension commit:

- `src/main/java/org/example/config/FactoryRegistry.java` — one registry entry, `rail -> RailFactory`.
- `src/test/java/org/example/AssignmentTests.java` — import and five rail tests.

New documentation: `docs/extension.md` (this file).

No changes were required to `MuseumDispatch`, `DispatchCreator`, product interfaces,
model records, `Main`, or any existing family. The registry is intentionally the
composition root where available implementations are wired together. The final
README and UML were added in a later documentation commit.
