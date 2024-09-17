CREATE TABLE users(
    id BIGINT PRIMARY KEY IDENTITY(1, 1),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    UNIQUE (username)
);


CREATE TABLE bookings(
    id BIGINT PRIMARY KEY IDENTITY(1, 1),
    user_id BIGINT,
    booking_date DATE,
    booking_item VARCHAR(100),
    status VARCHAR(20),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
)