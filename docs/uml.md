# UML class diagrams

The system is shown in three views for readability. Together they include all
product implementations, creators, factories and the client. `F` is the same
family type throughout each product kit. The model records are shown where
their role in compatibility matters.

## Abstract Factory part and client

```mermaid
classDiagram
direction TB
class Main {
  +main(args) void
  -demonstrate(factory) void
}
class FactoryRegistry {
  +select(name) TransportFactory
}
class MuseumDispatch~F~ {
  -Planner planner
  -Labeler labeler
  -IntakeScanner scanner
  -DispatchCreator creator
  +book(shipment, budget, days) Label
  +reroute(label, reading, destination, budget, days) Label
  +receive(label, reading) String
}
class TransportFactory~F~ {
  <<interface>>
  +createPlanner() Planner
  +createLabeler() Labeler
  +createIntakeScanner() IntakeScanner
  +createDispatchCreator() DispatchCreator
}
class Planner~F~ {
  <<interface>>
  +capacityKg() int
  +plan(shipment) Plan
}
class Labeler~F~ {
  <<interface>>
  +encode(plan) Label
}
class IntakeScanner~F~ {
  <<interface>>
  +safe(label, reading) boolean
}
class DispatchCreator~F~
class RoadFactory
class AirFactory
class SeaFactory
class RailFactory
Main ..> FactoryRegistry : selects at startup
Main ..> MuseumDispatch : runs scenario
FactoryRegistry ..> RoadFactory : registers
FactoryRegistry ..> AirFactory : registers
FactoryRegistry ..> SeaFactory : registers
FactoryRegistry ..> RailFactory : registers
TransportFactory <|.. RoadFactory
TransportFactory <|.. AirFactory
TransportFactory <|.. SeaFactory
TransportFactory <|.. RailFactory
MuseumDispatch ..> TransportFactory : constructor dependency
MuseumDispatch --> Planner : holds
MuseumDispatch --> Labeler : holds
MuseumDispatch --> IntakeScanner : holds
MuseumDispatch --> DispatchCreator : holds
TransportFactory ..> Planner : creates
TransportFactory ..> Labeler : creates
TransportFactory ..> IntakeScanner : creates
TransportFactory ..> DispatchCreator : creates
```

## Factory Method part

`prepare` calls the overridable `createPlanner` before checking the resulting
plan against the budget and deadline. This is the inheritance relationship
that implements Factory Method. The four concrete factories also provide
the matching concrete Creator.

```mermaid
classDiagram
direction TB
class DispatchCreator~F~ {
  <<abstract>>
  #createPlanner() Planner
  +prepare(shipment, budget, deadlineDays) Plan
}
class Planner~F~ {
  <<interface>>
}
class RoadDispatchCreator {
  #createPlanner() Planner
}
class AirDispatchCreator {
  #createPlanner() Planner
}
class SeaDispatchCreator {
  #createPlanner() Planner
}
class RailDispatchCreator {
  #createPlanner() Planner
}
DispatchCreator <|-- RoadDispatchCreator
DispatchCreator <|-- AirDispatchCreator
DispatchCreator <|-- SeaDispatchCreator
DispatchCreator <|-- RailDispatchCreator
DispatchCreator ..> Planner : business workflow uses
RoadDispatchCreator ..> RoadPlanner : creates
AirDispatchCreator ..> AirPlanner : creates
SeaDispatchCreator ..> SeaPlanner : creates
RailDispatchCreator ..> RailPlanner : creates
RoadFactory ..> RoadDispatchCreator : creates
AirFactory ..> AirDispatchCreator : creates
SeaFactory ..> SeaDispatchCreator : creates
RailFactory ..> RailDispatchCreator : creates
```

## All concrete products and family consistency

Each row of implementations binds `F` to its own marker: `Road`, `Air`,
`Sea` or `Rail`. All four marker classes implement `Family`. For example,
`RoadPlanner` implements `Planner<Road>` and returns `Plan<Road>`.

```mermaid
classDiagram
direction LR
class Planner~F~ {
  <<interface>>
}
class Labeler~F~ {
  <<interface>>
}
class IntakeScanner~F~ {
  <<interface>>
}
class Plan~F~ {
  <<record>>
  +Shipment shipment
  +int cost
  +int days
  +String handling
}
class Label~F~ {
  <<record>>
  +Plan plan
  +String payload
}
class Reading {
  <<record>>
  +int shockG
  +int pressureKpa
  +int humidityPercent
}
Planner <|.. RoadPlanner
Planner <|.. AirPlanner
Planner <|.. SeaPlanner
Planner <|.. RailPlanner
Labeler <|.. RoadLabeler
Labeler <|.. AirLabeler
Labeler <|.. SeaLabeler
Labeler <|.. RailLabeler
IntakeScanner <|.. RoadIntakeScanner
IntakeScanner <|.. AirIntakeScanner
IntakeScanner <|.. SeaIntakeScanner
IntakeScanner <|.. RailIntakeScanner
RoadFactory ..> RoadPlanner : creates
RoadFactory ..> RoadLabeler : creates
RoadFactory ..> RoadIntakeScanner : creates
AirFactory ..> AirPlanner : creates
AirFactory ..> AirLabeler : creates
AirFactory ..> AirIntakeScanner : creates
SeaFactory ..> SeaPlanner : creates
SeaFactory ..> SeaLabeler : creates
SeaFactory ..> SeaIntakeScanner : creates
RailFactory ..> RailPlanner : creates
RailFactory ..> RailLabeler : creates
RailFactory ..> RailIntakeScanner : creates
Planner ..> Plan : returns
Labeler ..> Plan : consumes same F
Labeler ..> Label : returns same F
IntakeScanner ..> Label : consumes same F
IntakeScanner ..> Reading : checks
Label --> Plan : contains
Plan --> Shipment : contains
RoadIntakeScanner ..> RoadLabeler : reconstructs manifest
AirIntakeScanner ..> AirLabeler : reconstructs manifest
SeaIntakeScanner ..> SeaLabeler : reconstructs manifest
RailIntakeScanner ..> RailLabeler : reconstructs manifest
```

## How to read relationships

- `<|..` — realization: a concrete class implements an interface.
- `<|--` — generalization: a concrete Creator extends the abstract Creator.
- `-->` — association: an object holds a reference to another object.
- `..>` — dependency: a class uses, creates or refers to another type.

The diagrams use Mermaid and can be viewed in a Markdown viewer that supports
Mermaid. Neither composition diamonds nor ownership/lifetime guarantees are
claimed for injected products.
