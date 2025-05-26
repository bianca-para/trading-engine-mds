CREATE TABLE orders
(
    order_id   BIGSERIAL PRIMARY KEY,
    user_id    UUID           NOT NULL,
    asset_id   BIGINT         NOT NULL,
    price      NUMERIC(19, 2) NOT NULL CHECK (price > 0),
    quantity   NUMERIC(19, 4) NOT NULL CHECK (quantity > 0),
    status     VARCHAR(255)   NOT NULL,
    type       VARCHAR(255)   NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_asset FOREIGN KEY (asset_id) REFERENCES asset (id) ON DELETE CASCADE
);
