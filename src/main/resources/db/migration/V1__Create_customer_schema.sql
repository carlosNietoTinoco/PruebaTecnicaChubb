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
    num_cta VARCHAR(15) NOT NULL UNIQUE,
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
CREATE INDEX idx_customer_num_cta ON customer(num_cta);
CREATE INDEX idx_customer_status ON customer(status_id);
CREATE INDEX idx_customer_country ON customer(country_id);

-- Insert initial catalog data
INSERT INTO gender (name) VALUES ('MALE'), ('FEMALE');
INSERT INTO status (name) VALUES ('ACTIVE'), ('INACTIVE');
INSERT INTO country (name) VALUES
    ('COLOMBIA'),
    ('CHILE'),
    ('ARGENTINA'),
    ('BRASIL'),
    ('ECUADOR'),
    ('MEXICO'),
    ('PANAMA');