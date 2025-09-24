# ACE Dataset Documentation

This directory contains several ACE (Area Control Error) datasets derived from real Statnett Primary Reserves data.

## Available Datasets

### 1. `complete-ace-dataset.json` (18,995 records, 2.1MB)
- **Source**: Complete Primary Reserves D-1 2025 data from Statnett
- **Period**: January 1, 2025 - September 21, 2025
- **Records**: 18,995 inter-area power flows
- **Format**: JSON array with InArea, OutArea, Quantity (MW), TimeStamp

### 2. `sample-ace-from-fcr-data.json` (30 records)
- **Source**: Manually curated from FCR data analysis
- **Period**: September 22, 2025 (morning hours 6:00-11:00)
- **Use**: Realistic test data for demonstrations

### 3. `sample-ace-data.json` (20 records)
- **Source**: Synthetic data based on Norwegian bidding zone patterns
- **Period**: September 22, 2025 (morning hours 6:00-10:45)
- **Use**: Basic testing and examples

## Data Generation Process

### FCR to ACE Conversion Method:
1. **Parse HTML Table**: Extract timestamp, area, FCR-N volume from Statnett XLS
2. **Group by Timestamp**: Collect all 5 Norwegian areas (NO1-NO5) per hour
3. **Calculate Imbalances**: Use FCR volume differences to infer power flows
4. **Generate Flows**: Create realistic inter-area flows based on:
   - Volume differences between adjacent areas
   - Historical flow patterns
   - Grid topology constraints

### Flow Logic:
- **Quantity Range**: 1-50 MW (realistic for inter-area balancing)
- **Flow Patterns**: Based on actual Norwegian power system behavior
- **Areas**: NO1 (Eastern), NO2 (Southern), NO3 (Central), NO4 (Northern), NO5 (Western)

## Usage in AKHQ

To test the ACE calculator system:

1. **Access AKHQ**: http://localhost:8081
2. **Navigate**: Topics → ace-corridor → Produce
3. **Copy Data**: From any dataset file, copy individual JSON objects
4. **Paste as Value**: In AKHQ message producer
5. **Send**: Each message will flow through ace-calculator → ace-area → ace-backend

## Expected ACE Processing

Each input flow like:
```json
{"InArea":"NO1","OutArea":"NO2","Quantity":250,"TimeStamp":"2025-09-22T09:00:00Z"}
```

Gets processed by ace-calculator into area imbalances:
```json
[
  {"Area":"NO1","Quantity":250,"TimeStamp":"2025-09-22T09:00:00Z"},
  {"Area":"NO2","Quantity":-250,"TimeStamp":"2025-09-22T09:00:00Z"}
]
```

## Data Quality

- **Realistic Volumes**: Based on actual FCR requirements
- **Temporal Consistency**: Hourly progression with realistic daily patterns
- **Grid Topology**: Flows respect Norwegian transmission system constraints
- **Balanced Flows**: Generated flows maintain system balance principles

## File Sizes
- Complete dataset: 2.1MB (suitable for full system testing)
- Sample datasets: <50KB each (suitable for development/demos)