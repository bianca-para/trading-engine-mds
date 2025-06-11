-- V4__Seed_Users.sql
INSERT INTO users(id, username, email, password, kyc_status, registered_date) VALUES
                                                                                  ('11111111-1111-1111-1111-111111111111','alice','alice@example.com','$2a$10$7qY...hashedpw','VERIFIED','2025-01-15'),
                                                                                  ('22222222-2222-2222-2222-222222222222','bob','bob@example.com','$2a$10$9Ux...hashedpw','VERIFIED','2025-02-10'),
                                                                                  ('33333333-3333-3333-3333-333333333333','carol','carol@example.com','$2a$10$ZmN...hashedpw','VERIFIED','2025-03-05');
