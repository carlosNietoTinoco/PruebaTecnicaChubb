-- Customer Microservice Database Schema
-- Version: 1.0
-- Description: Initial schema creation for customer management

-- Create gender catalog table
CREATE TABLE gender (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create status catalog table
CREATE TABLE status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create country catalog table
CREATE TABLE country (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create customer table
CREATE TABLE customer (
    customer_id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    gender_id BIGINT NOT NULL,
    num_cta VARCHAR(15) NOT NULL,
    status_id BIGINT NOT NULL,
    country_id BIGINT NOT NULL,
    activate_date TIMESTAMP,
    inactivate_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_customer_gender FOREIGN KEY (gender_id) REFERENCES gender(id),
    CONSTRAINT fk_customer_status FOREIGN KEY (status_id) REFERENCES status(id),
    CONSTRAINT fk_customer_country FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Create indexes for better performance
CREATE INDEX idx_customer_status ON customer(status_id);
CREATE INDEX idx_customer_country ON customer(country_id);

-- Insert initial catalog data
INSERT INTO gender (id, name) VALUES (1, 'MALE');
INSERT INTO gender (id, name) VALUES (2, 'FEMALE');
INSERT INTO status (id, name) VALUES (1, 'ACTIVE');
INSERT INTO status (id, name) VALUES (2, 'INACTIVE');
INSERT INTO country (id, name) VALUES (1, 'COLOMBIA');
INSERT INTO country (id, name) VALUES (2, 'CHILE');
INSERT INTO country (id, name) VALUES (3, 'ARGENTINA');
INSERT INTO country (id, name) VALUES (4, 'BRAZIL');
INSERT INTO country (id, name) VALUES (5, 'ECUADOR');
INSERT INTO country (id, name) VALUES (6, 'MEXICO');
INSERT INTO country (id, name) VALUES (7, 'PANAMA');