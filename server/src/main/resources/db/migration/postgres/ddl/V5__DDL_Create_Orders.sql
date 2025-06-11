-- V3__Create_Orders.sql
CREATE TABLE orders
(
    order_id   BIGSERIAL       PRIMARY KEY,
    user_id    UUID            NOT NULL REFERENCES users(id)   ON DELETE CASCADE,
    asset_id   BIGINT          NOT NULL REFERENCES asset(id)   ON DELETE CASCADE,
    price      NUMERIC(19, 2)  NOT NULL CHECK (price > 0),
    quantity   NUMERIC(19, 4)  NOT NULL CHECK (quantity > 0),
    status     VARCHAR(20)     NOT NULL CHECK (status IN ('OPEN','PARTIALLY_FILLED','FILLED','CANCELLED')),
    type       VARCHAR(10)     NOT NULL CHECK (type   IN ('BUY','SELL')),
    created_at TIMESTAMP       NOT NULL
);
