# CarService

REST API for a car workshop that manages customers and vehicles, built with Spring Boot, Spring Data JPA and H2.

## Requirements

* JDK 21

## Run the app

### Windows

```
.\run.cmd
```

### Mac/Linux

```
chmod +x run.sh
./run.sh
```

The API runs on http://localhost:8080. The H2 database is in-memory and is reset on every restart.

H2 console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:verkstad`, user: `sa`, no password)

## Endpoints

| Function | Method | URL |
|---|---|---|
| Create customer | POST | `/customers?name=Anna&phone=123456789` |
| Get customer ID by name | GET | `/customer-id?name=Anna` |
| List all customers with vehicles | GET | `/customers` |
| Create vehicle | POST | `/vehicles?registrationNumber=ABC123&brand=Volvo&model=V60&productionYear=2022` |
| List all vehicles | GET | `/vehicles` |
| List vehicles by brand | GET | `/vehicles-by-brand?brand=Volvo` |
| Add vehicle to customer | POST | `/addvehicletocustomer?customerId=1&vehicleId=1` |
| Update vehicle brand | PUT | `/vehicles/1/brand?brand=Saab` |
| List registration numbers and brands | GET | `/vehicle-brand-info` |

## Examples

Run in this order after a fresh start so the IDs match.

```
curl -X POST "http://localhost:8080/customers?name=Anna&phone=123456789"
curl -X POST "http://localhost:8080/customers?name=Bertil&phone=987654321"
curl "http://localhost:8080/customer-id?name=Anna"

curl -X POST "http://localhost:8080/vehicles?registrationNumber=ABC123&brand=Volvo&model=V60&productionYear=2022"
curl -X POST "http://localhost:8080/vehicles?registrationNumber=XYZ789&brand=Volvo&model=XC90&productionYear=2020"
curl "http://localhost:8080/vehicles"
curl "http://localhost:8080/vehicles-by-brand?brand=Volvo"

curl -X POST "http://localhost:8080/addvehicletocustomer?customerId=1&vehicleId=1"
curl "http://localhost:8080/customers"

curl -X PUT "http://localhost:8080/vehicles/1/brand?brand=Saab"
curl "http://localhost:8080/vehicle-brand-info"
```

Example response from `GET /customers` (customers without vehicles are included):

```json
[
  { "id": 1, "name": "Anna", "phoneNumber": "123456789",
    "vehicles": [ { "id": 1, "registrationNumber": "ABC123", "brand": "Volvo", "model": "V60", "productionYear": 2022 } ] },
  { "id": 2, "name": "Bertil", "phoneNumber": "987654321", "vehicles": [] }
]
```

Example response from `GET /vehicle-brand-info`:

```json
[
  { "registrationNumber": "ABC123", "brand": "Saab" },
  { "registrationNumber": "XYZ789", "brand": "Volvo" }
]
```