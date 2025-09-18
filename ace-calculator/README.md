# Ace Calculator

This is an example streams app showcasing how input flow data for imbalance (ACE) can be used to calculate imbalance for areas.
The app requires install of Java 24 to run and Maven to build.

**Example**

An input like:

```json
[{"InArea":"NO1","OutArea":"NO2","Quantity":300,"TimeStamp":"2025-01-01T00:00:00Z"}]
```

produces:

```json
[{"Area":"NO1","Quantity":300,"TimeStamp":"2025-01-01T00:00:00Z"},
 {"Area":"NO2","Quantity":-300,"TimeStamp":"2025-01-01T00:00:00Z"}]
```

## Build & Test

```bash
mvn -q -DskipTests=false test
```

## Build & Run App 

```bash
mvn spring-boot:run
```

## Config
- Input topic: `A` (override with `app.topic.input`)
- Output topic: `B` (override with `app.topic.output`)
- Bootstrap servers: `spring.kafka.bootstrap-servers`