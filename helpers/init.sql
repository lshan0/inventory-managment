\c postgres;

DROP DATABASE IF EXISTS inventory_management;
CREATE DATABASE inventory_management;

\c inventory_management;

CREATE TABLE inventory (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL UNIQUE,
    quantity INT NOT NULL,
    version INT NOT NULL
);
