-- V5__Seed_Orders.sql
INSERT INTO orders(user_id, asset_id, price, quantity, status, type, created_at) VALUES
                                                                                     ('11111111-1111-1111-1111-111111111111', 1, 100000.00, 0.0100, 'OPEN', 'BUY',    '2025-06-10T09:15:00'),
                                                                                     ('22222222-2222-2222-2222-222222222222', 1,  99500.00, 0.0050, 'OPEN', 'SELL',   '2025-06-10T09:16:00'),
                                                                                     ('33333333-3333-3333-3333-333333333333', 9,     0.20,   1000.0, 'OPEN', 'SELL',   '2025-06-10T09:17:00'),
                                                                                     ('11111111-1111-1111-1111-111111111111', 9,     0.18,    500.0, 'OPEN', 'BUY',    '2025-06-10T09:18:00');
