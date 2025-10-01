-- This file allow to write SQL commands that will be emitted in test and dev.
-- Sample users and addresses for testing

-- Insert sample users
INSERT INTO users (id, first_name, last_name, date_of_birth, email, job, created_at, updated_at) VALUES
(gen_random_uuid(), 'John', 'Doe', '1990-05-15', 'john.doe@example.com', 'Software Engineer', NOW(), NOW()),
(gen_random_uuid(), 'Jane', 'Smith', '1985-08-22', 'jane.smith@example.com', 'Product Manager', NOW(), NOW()),
(gen_random_uuid(), 'Mike', 'Johnson', '1992-12-03', 'mike.johnson@example.com', 'Data Analyst', NOW(), NOW()),
(gen_random_uuid(), 'Sarah', 'Wilson', '1988-03-17', 'sarah.wilson@example.com', 'UX Designer', NOW(), NOW()),
(gen_random_uuid(), 'David', 'Brown', '1995-07-09', 'david.brown@example.com', 'DevOps Engineer', NOW(), NOW());

-- Insert sample addresses (using subqueries to get user IDs) - one address per user
INSERT INTO addresses (id, street, city, state_province, postal_code, country, user_id) VALUES
(gen_random_uuid(), '123 Main St', 'New York', 'NY', '10001', 'USA', (SELECT id FROM users WHERE email = 'john.doe@example.com')),
(gen_random_uuid(), '456 Oak Ave', 'Los Angeles', 'CA', '90210', 'USA', (SELECT id FROM users WHERE email = 'jane.smith@example.com')),
(gen_random_uuid(), '789 Pine Rd', 'Chicago', 'IL', '60601', 'USA', (SELECT id FROM users WHERE email = 'mike.johnson@example.com')),
(gen_random_uuid(), '321 Elm St', 'Seattle', 'WA', '98101', 'USA', (SELECT id FROM users WHERE email = 'sarah.wilson@example.com')),
(gen_random_uuid(), '654 Maple Dr', 'Austin', 'TX', '73301', 'USA', (SELECT id FROM users WHERE email = 'david.brown@example.com'));