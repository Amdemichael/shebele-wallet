-- Create transaction_entries table for double-entry accounting
CREATE TABLE transaction_entries (
                                     id BIGSERIAL PRIMARY KEY,
                                     transaction_id VARCHAR(100) NOT NULL,
                                     account_msisdn VARCHAR(15) NOT NULL,
                                     type VARCHAR(10) NOT NULL CHECK (type IN ('DEBIT', 'CREDIT')),
                                     amount DECIMAL(19,2) NOT NULL,
                                     reference TEXT,
                                     status VARCHAR(20) NOT NULL DEFAULT 'POSTED',
                                     created_at TIMESTAMP NOT NULL
);

-- Create indexes for performance
CREATE INDEX idx_transaction_entries_account ON transaction_entries(account_msisdn);
CREATE INDEX idx_transaction_entries_transaction ON transaction_entries(transaction_id);
CREATE INDEX idx_transaction_entries_created ON transaction_entries(created_at DESC);

-- Create materialized view for balances
CREATE MATERIALIZED VIEW account_balances AS
SELECT
    account_msisdn,
    SUM(CASE WHEN type = 'CREDIT' THEN amount ELSE -amount END) as balance,
    MAX(created_at) as last_updated
FROM transaction_entries
WHERE status = 'POSTED'
GROUP BY account_msisdn;

CREATE UNIQUE INDEX idx_account_balances_msisdn ON account_balances(account_msisdn);

-- Function to refresh balances
CREATE OR REPLACE FUNCTION refresh_account_balances()
RETURNS TRIGGER AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY account_balances;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger to auto-refresh after new entries
CREATE TRIGGER refresh_balances_after_insert
    AFTER INSERT ON transaction_entries
    EXECUTE FUNCTION refresh_account_balances();