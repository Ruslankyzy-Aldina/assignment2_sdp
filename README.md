# Assignment 2 — Museum artifact transport

Java application using **Factory Method** and **Abstract Factory** to arrange
museum artifact shipments. Four transport families provide 12 concrete products:
planners, labelers and intake scanners.

## Run

Requires JDK 25. In IntelliJ IDEA, open `pom.xml` and run `org.example.Main`.
Set the program argument to `road`, `air`, `sea` or `rail` (default: `road`).

Alternatively, run from PowerShell:

```powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Family road
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Test
```

The 36 automated tests use a dependency-free runner. They can also be launched
through `org.example.AssignmentTests`. `mvn test` does not execute this runner.

## Design

- **Factory Method:** `DispatchCreator.prepare` checks capacity, budget and
  deadline. Subclasses override `createPlanner` to choose the concrete planner.
- **Abstract Factory:** `TransportFactory<F>` creates a matching planner,
  labeler, scanner and dispatch creator.
- **Compatibility:** the family parameter `F` is shared by products, plans,
  labels and the client. For example, `AirLabeler` cannot accept `Plan<Road>`.
  An automated compilation test verifies this restriction.
- **Runtime selection:** `FactoryRegistry` selects a factory using the command
  line argument. `MuseumDispatch` works through abstractions.
- **Business operations:** booking checks delivery constraints and issues a
  label; rerouting checks the crate and issues a new label; arrival inspection
  verifies the plan, label and sensor readings before acceptance or quarantine.

## Transport rules

These are example rules for the assignment.

| Family | Capacity kg | Cost | Days | Max shock g | Min pressure kPa | Max humidity % |
|---|---:|---|---:|---:|---:|---:|
| Road | 500 | 40 + 2 × kg | 3 | 3 | 80 | 65 |
| Air | 100 | 200 + 8 × kg | 1 | 2 | 90 | 55 |
| Sea | 2000 | 100 + kg | 14 | 4 | 75 | 45 |
| Rail | 1000 | 70 + kg | 5 | 2 | 80 | 60 |

## Assignment materials

- [Part A — problems with direct creation](docs/part-a.md)
- [Part G — exact changes for the fourth family](docs/extension.md)
- [UML — Abstract Factory and client](docs/uml-images/01-abstract-factory.png)
- [UML — Factory Method](docs/uml-images/02-factory-method.png)
- [UML — all concrete products](docs/uml-images/03-products.png)

The initial implementation is retained in `legacy/DirectDispatch.java` and in
Git history. View development stages with `git log --oneline --reverse`.
