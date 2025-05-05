CREATE TABLE roles
(
    user_id   UUID NOT NULL,
    user_role VARCHAR(255),
    CONSTRAINT fk_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);