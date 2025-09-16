
-- Test data for CustomerControllerIntegrationTest


-- Inactive customer for activate test (testActivateCustomer_Success)
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date, inactivate_date) VALUES (
    X'a1b2c3d4e5f678901234567890abcdef',
    'Carlos Rodríguez',
    '1985-03-10',
    1, -- MALE
    '111222333444',
    2, -- INACTIVE
    1, -- COLOMBIA (Asumiendo que 1 es el ID para Colombia)
    NULL, -- No tiene fecha de activación si está inactivo
    CURRENT_TIMESTAMP
);

-- Active customer for deactivate test (testDeactivateCustomer_Success)
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

-- Already inactive customer for deactivate fail test (testDeactivateCustomer_Fail_AlreadyInactive)
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date, inactivate_date) VALUES (
    X'b2c3d4e5f678901234567890abcdef12',
    'María González',
    '1992-07-25',
    2, -- FEMALE
    '555666777888',
    2, -- INACTIVE
    2, -- CHILE (Asumiendo que 2 es el ID para Chile)
    NULL,
    CURRENT_TIMESTAMP
);

-- Already active customer for activate fail test (testActivateCustomer_Fail_AlreadyActive)
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date) VALUES (
    X'c3d4e5f678901234567890abcdef1234',
    'Pedro Martínez',
    '1988-11-30',
    1, -- MALE
    '999888777666',
    1, -- ACTIVE
    3, -- ARGENTINA (Asumiendo que 3 es el ID para Argentina)
    CURRENT_TIMESTAMP
);

-- Active customer for update test (testUpdateCustomer_Success)
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date) VALUES (
    X'd4a7f8b19c3e4a5db1e2f3c4d5a6b7c8',
    'Juan Pérez Original',
    '1992-01-01',
    1, -- MALE
    '123123123123',
    1, -- ACTIVE
    1, -- COLOMBIA
    CURRENT_TIMESTAMP
);

-- Inactive customer dedicated to the update failure test (testUpdateCustomer_Fail_Inactive)
INSERT INTO customer (customer_id, name, birth_date, gender_id, num_cta, status_id, country_id, activate_date, inactivate_date) VALUES (
    X'f1a2b3c4d5e6f7890123456789abcdef',
    'Inactive User For Update Test',
    '1991-01-01',
    1, -- MALE
    '987987987987',
    2, -- INACTIVE
    1, -- COLOMBIA
    NULL,
    CURRENT_TIMESTAMP
);