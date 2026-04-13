CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              transaction_ref VARCHAR(100) NOT NULL UNIQUE,
                              from_msisdn VARCHAR(15) NOT NULL,
                              to_msisdn VARCHAR(15) NOT NULL,
                              amount DECIMAL(19,2) NOT NULL,
                              fee DECIMAL(19,2) NOT NULL DEFAULT 0,  -- ← ADD THIS LINE
                              description TEXT,
                              status VARCHAR(20) NOT NULL,
                              created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_transactions_from ON transactions(from_msisdn);
CREATE INDEX idx_transactions_to ON transactions(to_msisdn);
CREATE INDEX idx_transactions_created ON transactions(created_at DESC);