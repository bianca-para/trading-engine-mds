-- DOAR DE TEST, O SA LE INLOCUIESC CU  MIGRATII FLYWAY
CREATE TABLE IF NOT EXISTS asset
(
    id     BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(10)      NOT NULL UNIQUE,
    name   VARCHAR(50)      NOT NULL,
    price  DOUBLE PRECISION NOT NULL
    );

CREATE TABLE IF NOT EXISTS users
(
    id              UUID PRIMARY KEY,
    username        VARCHAR(32)  NOT NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    kyc_status      VARCHAR(255),
    registered_date DATE
    );


CREATE TABLE IF NOT EXISTS user_asset
(
    user_asset_id BIGSERIAL PRIMARY KEY,
    user_id       UUID   NOT NULL,
    asset_id      BIGINT NOT NULL,
    quantity      NUMERIC(20, 8) DEFAULT 0,

    CONSTRAINT fk_user_asset_user FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE,

    CONSTRAINT fk_user_asset_asset FOREIGN KEY (asset_id)
    REFERENCES asset (id)
    ON DELETE CASCADE,

    CONSTRAINT uq_users_asset UNIQUE (user_id, asset_id)
    );