CREATE TABLE transactions (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    type VARCHAR(50) NOT NULL,
    category VARCHAR(50),

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    original_amount NUMERIC(19, 2) NOT NULL,
    original_currency CHAR(3) NOT NULL,

    converted_amount NUMERIC(19, 2),
    converted_currency CHAR(3),

    destination VARCHAR(255),
    description TEXT,

    external BOOLEAN NOT NULL DEFAULT FALSE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    rejection_reason TEXT
);

CREATE INDEX idx_transactions_user_id
    ON transactions (user_id);

CREATE INDEX idx_transactions_created_at
    ON transactions (created_at);

CREATE INDEX idx_transactions_status
    ON transactions (status);


