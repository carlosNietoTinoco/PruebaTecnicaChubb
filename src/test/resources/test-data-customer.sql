
-- Test data for CustomerControllerIntegrationTest
-- Data for testActivateCustomer_Success test

-- Inactive customer for activate test
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date, inactivate_date) VALUES (
    X'a1b2c3d4e5f678901234567890abcdef',
    'Carlos Rodríguez',
    '1985-03-10',
    1, -- MALE
    '111222333444',
    2, -- INACTIVE
    CURRENT_TIMESTAMP
);

INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date) VALUES (
    X'e1f2a3b4c5d67890abcdef1234567890',
    'Ana López',
    '1990-05-15',
    2, -- FEMALE
    '876543210987',
    1, -- ACTIVE
    1, -- COLOMBIA
    CURRENT_TIMESTAMP
);

-- Inactive customer for activate test
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date, inactivate_date) VALUES (
    X'a1b2c3d4e5f678901234567890abcdef',
    'Carlos Rodríguez',
    '1985-03-10',
    1, -- MALE
    '111222333444',
    2, -- INACTIVE
    1, -- COLOMBIA
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Already inactive customer for deactivate fail test
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date, inactivate_date) VALUES (
    X'b2c3d4e5f678901234567890abcdef12',
    'María González',
    '1992-07-25',
    2, -- FEMALE
    '555666777888',
    2, -- INACTIVE
    2, -- CHILE
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Already active customer for activate fail test
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date) VALUES (
    X'c3d4e5f678901234567890abcdef1234',
    'Pedro Martínez',
    '1988-11-30',
    1, -- MALE
    '999888777666',
    1, -- ACTIVE
    3, -- ARGENTINA
    CURRENT_TIMESTAMP
);