CREATE TABLE wallet_transaction (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    balance DECIMAL(18,2) NOT NULL,
    created_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);
