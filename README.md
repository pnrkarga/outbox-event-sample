# Outbox Event Sample

This project is a sample implementation of the **Outbox Pattern** using Spring Boot, PostgreSQL, and RabbitMQ. It demonstrates a reliable way to publish domain events to a message broker **after** successful database transactions, ensuring **eventual consistency** in microservices.

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Testcontainers (for integration testing)
- Docker Compose (for local development)

## 📦 Modules Overview

- `outbox-event-core`: Contains shared logic for persisting and publishing events.
- `event-publisher-service`: Persists domain data and related outbox events in a single transaction.
- `event-relay-service`: Periodically polls the outbox table and publishes events to RabbitMQ.

## ✅ Features

- Implements the transactional outbox pattern using polling.
- Ensures messages are published to RabbitMQ only after a successful database commit.
- Supports error handling and retrying failed messages.
- Includes Docker-based setup for PostgreSQL and RabbitMQ.
- Integration tests using Testcontainers.

## 📁 Project Structure
outbox-event-sample/
├── outbox-event-core/ # Common event model and outbox logic
├── event-publisher-service/ # Domain logic and outbox writer
├── event-relay-service/ # Polls and publishes to RabbitMQ
├── docker-compose.yml # Local PostgreSQL and RabbitMQ setup
└── README.md

## 🚀 Getting Started

### Prerequisites

- Docker and Docker Compose
- Java 17+
- Maven

### Start Services Locally

Run the infrastructure with Docker:

```bash
docker-compose up -d