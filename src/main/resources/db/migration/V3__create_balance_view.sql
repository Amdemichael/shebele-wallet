CREATE MATERIALIZED VIEW account_balance_view AS
SELECT
    msisdn,
    balance,
    full_name,
    account_number,
    updated_at as last_updated
FROM accounts
WHERE status = 'ACTIVE';

CREATE UNIQUE INDEX idx_balance_view_msisdn ON account_balance_view(msisdn);