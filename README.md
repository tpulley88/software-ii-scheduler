# Scheduling Management System

A JavaFX desktop application for managing customers and appointments across time zones. The project demonstrates a layered DAO architecture, prepared SQL statements, localized UI resources, scheduling validation, and operational reports.

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

The application expects a local MySQL database named `client_schedule`. Database credentials are never stored in source control. Set these environment variables before launching:

```text
SCHEDULER_DB_USER
SCHEDULER_DB_PASSWORD
```

The original classroom database schema is not included. A compatible local schema is required to exercise database-backed features.

## Project context

Originally created by Terin Pulley in 2021 as a university Software II project. It is preserved as a portfolio example of Java desktop application architecture. No grading rubric, assessment prompt, production credentials, or customer data is included.

## Privacy

Use fictional data only. The repository contains no production database or real customer records.
