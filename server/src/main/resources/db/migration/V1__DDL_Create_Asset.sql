CREATE TABLE asset
(
    id     SERIAL PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    name   VARCHAR(50) NOT NULL,
    price  DOUBLE PRECISION CHECK (price >= 0)
);