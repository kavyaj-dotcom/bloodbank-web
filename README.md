# Intelligent Blood Bank Management System

A web application for managing blood inventory and finding donors in emergencies. Built with Java, Spring Boot, Thymeleaf and MySQL as an academic project.

## How it works

The system follows an **inventory-first** workflow:

1. A hospital or patient request is submitted (blood group, Rh factor, units, component, emergency flag).
2. The system checks the blood inventory first.
3. If enough stock exists, the request is fulfilled from inventory.
4. Only if stock is short or unavailable does the system search for donors.

Donor matching is rule-based (not machine learning) and considers:

- Blood group compatibility
- Donor eligibility
- Minimum 90-day gap since the last donation
- Current availability
- Proximity (donors in the hospital's city are ranked higher)

## Modules

| Module | What it does |
| --- | --- |
| Blood Inventory Management | Add stock, view totals by blood group, track batches and expiry |
| Patient / Hospital Request | Submit a request and run the inventory check |
| Smart Donor Matching | Filter and rank eligible donors when stock is short |
| Emergency Notification | Log a notification for each eligible donor |

Donor registration is also included.

## Tech stack

- Java (JDK 21 or later recommended)
- Spring Boot with Thymeleaf
- MySQL, accessed with plain JDBC (no JPA/ORM)
- Maven (wrapper included)

## Setup

### 1. Prerequisites

- JDK installed
- MySQL Server running locally on port 3306

### 2. Create the database

Create the database and tables by running `schema.sql` in MySQL Workbench or the MySQL command line:

```sql
CREATE DATABASE blood_bank_db;
USE blood_bank_db;
-- then run the contents of schema.sql
```

The database uses these tables: `donor`, `blood_inventory`, `blood_request`, `donation_history`, `notification_log`.

### 3. Set the database password

The app reads the MySQL password from an environment variable, so no password is stored in the code. The username is `root` (change it in `DBConnection.java` if yours is different).

**Windows (PowerShell):**

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

**macOS / Linux:**

```bash
export DB_PASSWORD="your_mysql_password"
```

In IntelliJ you can instead add `DB_PASSWORD` under Run → Edit Configurations → Environment variables.

### 4. Run the app

From the project folder:

```bash
./mvnw spring-boot:run
```

On Windows PowerShell use `.\mvnw spring-boot:run`.

Then open http://localhost:8080 in your browser.

## Project structure

```
src/main/java/com/bloodbank/
  controller/   web controllers
  dao/          JDBC data access classes
  service/      donor matching logic
  util/         DBConnection
src/main/resources/
  templates/    Thymeleaf pages
  static/       CSS
```

## Notes

- All sample data should be fictional. Do not commit real donor or patient details.
- This is a student project for learning and demonstration, not a medical system.
