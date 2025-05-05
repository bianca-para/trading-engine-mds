CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE TABLE users
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- Requires pgcrypto or use application-generated UUIDs
    username        VARCHAR(32)         NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    password        TEXT                NOT NULL,
    kyc_status      VARCHAR(255),
    registered_date DATE,
    CONSTRAINT chk_username_length CHECK (char_length(username) >= 1 AND char_length(username) <= 32)
);