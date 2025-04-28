
-- DOAR DE TEST, O SA LE INLOCUIESC CU  MIGRATII FLYWAY
INSERT INTO asset (symbol, name, price)
VALUES ('BTC', 'Bitcoin', 30000.00),
       ('ETH', 'Ethereum', 2000.00),
       ('ADA', 'Cardano', 0.50);

INSERT INTO users (id, username, email, password, kyc_status, registered_date)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 'john_doe', 'john@example.com', 'password123', 'PENDING', CURRENT_DATE),
       ('223e4567-e89b-12d3-a456-426614174001', 'jane_smith', 'jane@example.com', 'password456', 'APPROVED',
        CURRENT_DATE);

INSERT INTO user_asset (user_id, asset_id, quantity)
VALUES ('123e4567-e89b-12d3-a456-426614174000', 1, 0.5), -- john_doe owns 0.5 BTC
       ('123e4567-e89b-12d3-a456-426614174000', 2, 10),  -- john_doe owns 10 ETH
       ('223e4567-e89b-12d3-a456-426614174001', 3, 1500); -- jane_smith owns 1500 ADA
