-- V2__Create_Users.sql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username        VARCHAR(32)    NOT NULL,
    email           VARCHAR(255)   NOT NULL UNIQUE,
    password        TEXT           NOT NULL,
    kyc_status      VARCHAR(20)    NOT NULL CHECK (kyc_status IN ('PENDING','VERIFIED','REJECTED')),
    registered_date DATE           NOT NULL,
    CONSTRAINT chk_username_length
        CHECK (char_length(username) BETWEEN 1 AND 32)
);
