Inventory Management System

This project implements a producer-consumer system using RabbitMQ and PostgreSQL. The producer sends inventory events to RabbitMQ, and the consumer processes these events to update inventory records in a PostgreSQL database.
Project Structure

    producer: Contains the code for the event producer.
    consumer: Contains the code for the event consumer.
    utils: Contains utility classes and methods used by both producer and consumer.
    Docker: Includes Dockerfiles and a Docker Compose configuration for setting up the services.

Getting Started
Prerequisites

    Docker (for running RabbitMQ and PostgreSQL)
    Java JDK 17
    Maven

Setup

 Clone the Repository

    git clone https://github.com/yourusername/inventory-managment.git
    cd inventory-managment

Build and Run Docker Containers

 Docker Compose is used to manage all modules of the project i.e. Producer, Consumer, PostgreSQL and RabbitMQ.

    docker-compose up --build

The command will build the Docker images for all modules, and they will be automatically run by Docker.

Configuration

    RabbitMQ Configuration
        Host: rabbitmq
        Port: 5672
        Username: bosch
        Password: very_secret

    PostgreSQL Configuration
        Host: postgres
        Port: 5432
        Database: inventory_management
        Username: bosch
        Password: very_secret

Building the Project

To build the project locally, navigate to the root directory and use Maven:

bash

    mvn clean install

This command will compile the code, run tests, and package the application.

Running Locally

If you prefer to run the producer and consumer services locally without Docker:

    Start RabbitMQ and PostgreSQL services locally or use the Docker Compose setup.

    Run the Producer and Consumer classes with the appropriate RabbitMQ and PostgreSQL configurations.
