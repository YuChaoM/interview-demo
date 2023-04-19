CREATE TABLE user_wallet
(
    id           BIGINT   NOT NULL AUTO_INCREMENT,
    user_id      BIGINT   NOT NULL,
    balance      DECIMAL(18, 2) DEFAULT 0.00,
    created_time DATETIME NOT NULL,
    updated_time DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (user_id)
);

