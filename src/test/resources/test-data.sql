-- Test data for unit and integration tests

-- Insert test users
INSERT INTO users (id, first_name, last_name, date_of_birth, email, job, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'Test', 'User', '1990-01-01', 'test.user@example.com', 'Test Engineer', NOW(), NOW()),
('22222222-2222-2222-2222-222222222222', 'Jane', 'Doe', '1985-05-15', 'jane.doe@example.com', 'Product Manager', NOW(), NOW()),
('33333333-3333-3333-3333-333333333333', 'Bob', 'Smith', '1992-12-25', 'bob.smith@example.com', 'Developer', NOW(), NOW());

-- Insert test addresses
INSERT INTO addresses (id, street, city, state_province, postal_code, country, user_id) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '123 Test St', 'Test City', 'TC', '12345', 'Test Country', '11111111-1111-1111-1111-111111111111'),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '456 Main Ave', 'Sample City', 'SC', '67890', 'Sample Country', '22222222-2222-2222-2222-222222222222');