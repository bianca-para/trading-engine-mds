CREATE TABLE trade
(
    trade_id      UUID PRIMARY KEY DEFAULT gen_random_uuid(), -- or use UUID from application
    buyer_id      UUID           NOT NULL,
    seller_id     UUID           NOT NULL,
    buy_order_id  BIGINT         NOT NULL,
    sell_order_id BIGINT         NOT NULL,
    asset_id      BIGINT         NOT NULL,
    price         NUMERIC(19, 2) NOT NULL CHECK (price > 0),
    quantity      NUMERIC(19, 4) NOT NULL CHECK (quantity > 0),
    executed_at   TIMESTAMP,

    CONSTRAINT fk_trade_buyer FOREIGN KEY (buyer_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_trade_seller FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_trade_buy_order FOREIGN KEY (buy_order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_trade_sell_order FOREIGN KEY (sell_order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_trade_asset FOREIGN KEY (asset_id) REFERENCES asset (id) ON DELETE CASCADE
);