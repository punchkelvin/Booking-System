CREATE TABLE refresh_token(
    id BIGINT PRIMARY KEY IDENTITY(1, 1),
    user_id BIGINT NOT NULL,
    refresh_token VARCHAR(255) NOT NULL UNIQUE,
    created_date DATETIME2 NOT NULL,
    expiry_date DATETIME2 NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)
)