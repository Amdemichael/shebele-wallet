CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          msisdn VARCHAR(15) NOT NULL UNIQUE,
                          account_number VARCHAR(20) NOT NULL UNIQUE,
                          full_name VARCHAR(100) NOT NULL,
                          balance DECIMAL(19,2) NOT NULL DEFAULT 0,
                          status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP
);

CREATE INDEX idx_accounts_msisdn ON accounts(msisdn);