# Scheduling Management System

A JavaFX desktop application for managing customers and appointments across time zones. The project demonstrates a layered DAO architecture, prepared SQL statements, localized UI resources, scheduling validation, and operational reports.

> Portfolio note: this is a sanitized snapshot of a 2021 university project, preserved to demonstrate Java desktop architecture rather than current production design.

## Highlights

- Customer and appointment create, read, update, and delete workflows
- Appointment collision and business-hours validation
- Time-zone-aware scheduling
- English and French interface resources
- Appointment, contact schedule, and country reports
- Login-attempt logging
- JavaFX views separated from model and data-access layers

## Technology

- Java 11
- JavaFX 11
- MySQL Connector/J 8
- JUnit

## Local configuration

With no database environment variables, the application starts in portfolio demo mode using a seeded, in-memory H2 database containing fictional records. Sign in with username `demo` and password `demo`; changes disappear when the application exits.

To use a local MySQL database named `client_schedule` instead, set these environment variables before launching:

```text
SCHEDULER_DB_USER
SCHEDULER_DB_PASSWORD
```

The original classroom database schema is not included. A compatible local schema is required only for the MySQL-backed mode.

With JDK 11 installed, compile and test the application with:

```text
./gradlew clean test classes
```

After configuring the database and environment variables, launch it with:

```text
./gradlew run
```

The Gradle build resolves JavaFX, MySQL Connector/J, and JUnit automatically.

## Project context

Originally created by Terin Pulley in 2021 as a university Software II project. It is preserved as a portfolio example of Java desktop application architecture. No grading rubric, assessment prompt, production credentials, or customer data is included.

## Privacy

Use fictional data only. The repository contains no production database or real customer records.

The public repository starts with this sanitized portfolio edition; the original classroom history was intentionally not imported.
