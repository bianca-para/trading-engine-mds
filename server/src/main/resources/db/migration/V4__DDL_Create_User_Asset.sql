CREATE TABLE user_asset
(
    user_asset_id SERIAL PRIMARY KEY,
    user_id       UUID    NOT NULL,
    asset_id      BIGINT  NOT NULL,
    quantity      NUMERIC NOT NULL DEFAULT 0,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_asset FOREIGN KEY (asset_id) REFERENCES asset (id) ON DELETE CASCADE
);