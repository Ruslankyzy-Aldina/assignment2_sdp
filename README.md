# Assignment 2 — Museum artifact transport

Java application demonstrating **Factory Method** and **Abstract Factory** for
transporting museum artifact crates between exhibition sites. Four transport
families supply three related products each: a planner, a manifest labeler and
an intake scanner. The project contains 12 concrete products and 36 automated tests.

## Запуск

Нужен JDK 25 (в текущем проекте уже настроен). Дополнительные библиотеки и Maven
для запуска не требуются. В PowerShell из папки проекта:

```powershell
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Family road
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Family air
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Family sea
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Family rail
powershell -ExecutionPolicy Bypass -File .\run.ps1 -Test
```

В IntelliJ IDEA открой `pom.xml`, выбери JDK 25 и запусти `org.example.Main`.
В Program arguments укажи `road`, `air`, `sea` или `rail`. По умолчанию — `road`.
Тесты также запускаются через `org.example.AssignmentTests` в `src/test/java`.
Используется собственный небольшой test runner, а не JUnit; команда `mvn test`
сама по себе эти тесты не выполняет. Полная проверка — `run.ps1 -Test`.

Пример для `rail`:

```text
BOOKED RAIL#ART-101|SITE-A|90|Vibration isolated rail crate
Delivery days: 5
REROUTED RAIL#ART-101|SITE-B|90|Vibration isolated rail crate
ACCEPTED ART-101
QUARANTINE ART-101
```

## Предметная область

Планировщик рассчитывает цену, срок и требования к упаковке. Маркировщик
записывает план в транспортный манифест. Сканер сверяет манифест и показания
датчиков при приёмке. Правила семейств различаются из-за условий перевозки.
Числа ниже — учебные бизнес-правила, не реальные транспортные нормативы.

| Семейство | Вместимость кг | Цена в условных единицах | Дни | Максимальный удар g | Минимальное давление kPa | Максимальная влажность % |
|---|---:|---|---:|---:|---:|---:|
| Road | 500 | 40 + 2 × kg | 3 | 3 | 80 | 65 |
| Air | 100 | 200 + 8 × kg | 1 | 2 | 90 | 55 |
| Sea | 2000 | 100 + kg | 14 | 4 | 75 | 45 |
| Rail | 1000 | 70 + kg | 5 | 2 | 80 | 60 |

## Соответствие требованиям

| Часть | Реализация |
|---|---|
| A | `legacy/DirectDispatch.java`, объяснение проблем в `docs/part-a.md`, исходный коммит `f62028f` |
| B | `creators/DispatchCreator.java` и четыре конкретных Creator |
| C | `factories/TransportFactory.java` и четыре конкретных фабрики |
| D | Общий параметр `F extends Family` у продуктов, планов, манифестов и клиента |
| E | Аргумент командной строки и `config/FactoryRegistry.java` |
| F | `client/MuseumDispatch.java`: `book`, `reroute`, `receive` |
| G | Семейство Rail и точный перечень изменений в `docs/extension.md` |
| H | UML в `docs/uml.md` |
| I | 36 автоматических тестов в `src/test/java/org/example/AssignmentTests.java` |
| J | Последовательные содержательные коммиты; просмотр через `git log --oneline --reverse` |

Все пути Java в таблице относятся к `src/main/java/org/example/`.

## Как работают паттерны

**Factory Method.** Абстрактный `DispatchCreator<F>` содержит метод `prepare`:
проверяет параметры заказа, грузоподъёмность, бюджет и срок. Для получения
планировщика он вызывает защищённый абстрактный метод `createPlanner()`.
`RoadDispatchCreator`, `AirDispatchCreator`, `SeaDispatchCreator` и
`RailDispatchCreator` переопределяют этот метод. Выбор продукта происходит
через полиморфизм наследников, поэтому это Factory Method, а не статическая
фабрика. Полезный алгоритм находится в `prepare`, а не в самом конструкторе.

**Abstract Factory.** `TransportFactory<F>` создаёт согласованный комплект
из `Planner<F>`, `Labeler<F>` и `IntakeScanner<F>`. Дополнительный метод
`createDispatchCreator` предоставляет подходящий workflow Factory Method.
`MuseumDispatch<F>` получает фабрику через конструктор и хранит только
абстракции. Конкретные реализации выбираются один раз в `FactoryRegistry`.

**Совместимость.** `Road` и `Air` — разные типы-маркеры. Метод
`AirLabeler.encode` принимает только `Plan<Air>`, поэтому передать ему
`Plan<Road>` нельзя. Клиент сохраняет один параметр `F` во всех операциях.
После выбора `TransportFactory<?>` метод `Main.demonstrate` захватывает
неизвестный тип в свой параметр `F` и сохраняет согласованность.
В бизнес-логике нет `switch` по семействам, `instanceof` или проверки
`wrongFamily`. Как обычно в Java, намеренное использование raw types,
непроверяемых приведений или reflection выходит за рамки этой гарантии.
Проект компилируется с `-Xlint:all -Werror` без предупреждений.

## Три бизнес-операции

1. **Бронирование `book`.** Creator и Planner проверяют возможность доставки,
   затем Labeler создаёт манифест с ценой, пунктом назначения и упаковкой.
2. **Перенаправление `reroute`.** Scanner проверяет состояние и манифест.
   Для безопасного груза Creator и Planner рассчитывают новую отправку,
   Labeler выдаёт новый манифест. Исходная запись остаётся неизменной.
3. **Приёмка `receive`.** Planner восстанавливает ожидаемый план, Labeler
   восстанавливает ожидаемый манифест, Scanner проверяет датчики.
   Результат — принятие или карантин. Проверка целостности учебная:
   криптографическая подпись и постоянное хранение не реализованы.

## Проверки

Тесты проверяют все конкретные продукты, совместную работу комплектов,
выбор во время запуска, три бизнес-операции, границы бюджета и сроков,
перегруз, неверные параметры, повреждения и подмену манифеста.
Отдельный тест вызывает компилятор Java и подтверждает, что смешивание
`Plan<Road>` с `AirLabeler` завершается ошибкой типов.
Ещё один тест подставляет собственную тестовую фабрику и выполняет все три
операции: это доказывает работу клиента через абстракции. Пять тестов
добавлены вместе с Rail. Проверка прекращается с ненулевым кодом при ошибке.

## Для защиты

Короткие ответы и порядок демонстрации находятся в `docs/defense-ru.md`.
