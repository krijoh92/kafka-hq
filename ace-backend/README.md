# Ace Backend

This is a backend for frontend app (BFF) that listens for Ace data on Kafka -> stores in db -> exposes http rest endpoints
to fetch stored data. 


## Input (Kafka topic)
  ```json
  [
    {"Area":"NO1","Quantity":300,"TimeStamp":"2025-01-01T00:00:00Z"},
    {"Area":"NO2","Quantity":-300,"TimeStamp":"2025-01-01T00:00:00Z"}
  ]
  ```

- Exposes data through f.ex `GET /api/timeseries?area=NO1&start=2025-01-01T00:00:00Z&end=2025-01-02T00:00:00Z`

## Output (HTTP GET REQUEST)

- F.ex `GET /api/timeseries?area=NO1&start=2025-01-01T00:00:00Z&end=2025-01-02T00:00:00Z`

```json
{
  "area": "NO1",
  "series": [
    { "timestamp": "2025-01-01T00:00:00Z", "value": 300 }
  ]
}
```

## Run
Build & run:
```bash
./mvnw spring-boot:run
```

## H2 console (DB)
Visit `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:timeseriesdb`).


