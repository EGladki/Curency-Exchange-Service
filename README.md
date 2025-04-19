# Currency Exchange API

A lightweight Java-based REST API for managing currencies and exchange rates. Built using Servlets, JDBC (with HikariCP), and SQLite. No frameworks.

## Features

- Add and retrieve currencies
- Add, retrieve, and update exchange rates
- Convert currency amounts (direct, reversed, and cross rates)
- JSON-based request/response structure
- Centralized error handling
- Input validation

## Technologies

- Java
- JDBC with HikariCP
- Servlets (no frameworks)
- SQLite
- Gson for JSON serialization
- Tomcat (for deployment)

## Endpoints

### Currencies
- `GET /currencies` – Get all currencies
- `GET /currency` – Get currency 
- `POST /currencies` – Add a new currency  

### Exchange Rates
- `GET /exchangeRates` – Get all exchange rates
- `GET /exchangeRate` – Get exchange rate  
- `POST /exchangeRates` – Add a new exchange rate  
- `PATCH /exchangeRate` – Update a single exchange rate  
