Inventory Management System
Overview

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

    git clone https://github.com/yourusername/your-repo.git
    cd your-repo

Build and Run Docker Containers

 Docker Compose is used to manage RabbitMQ and PostgreSQL services.

    docker-compose up --build

    This command will build the Docker images for the producer and consumer services, and start the RabbitMQ and PostgreSQL containers.

    Run the Producer and Consumer

    The producer and consumer services will be started automatically by Docker Compose. Ensure that both services are running and properly connected to RabbitMQ and PostgreSQL.

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

    Update application.properties in the producer and consumer modules with the appropriate RabbitMQ and PostgreSQL configurations.

    Run the Producer and Consumer

    In the producer module:

    bash

mvn exec:java -Dexec.mainClass="com.bosch.coding.Producer"

In the consumer module:

bash

    mvn exec:java -Dexec.mainClass="com.bosch.coding.FruitConsumer"

Testing

Ensure that the RabbitMQ and PostgreSQL services are running, and use the provided test cases to validate the functionality of the producer and consumer.
Contributing

Contributions are welcome! Please open an issue or a pull request with your suggestions or improvements.