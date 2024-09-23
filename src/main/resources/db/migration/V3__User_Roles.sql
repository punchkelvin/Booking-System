CREATE TABLE user_roles(
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE --ON Delete cascade ensures that the role is deleted when the user is deleted
)

ALTER TABLE users
DROP COLUMN role;