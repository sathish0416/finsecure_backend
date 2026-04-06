-- Database migration script for FinSecure backend improvements
-- Run this script to update your PostgreSQL database

-- Add audit fields to existing tables
ALTER TABLE transactions 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE users 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Update existing records with created_at timestamps
UPDATE transactions SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE transactions SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
UPDATE users SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;

-- Add constraints for better data integrity
ALTER TABLE transactions 
ADD CONSTRAINT chk_transaction_amount_positive CHECK (amount > 0),
ADD CONSTRAINT chk_transaction_date_not_future CHECK (date <= CURRENT_DATE);

ALTER TABLE users 
ADD CONSTRAINT chk_user_email_length CHECK (LENGTH(email) >= 5),
ADD CONSTRAINT chk_user_name_length CHECK (LENGTH(name) >= 2);

-- Add indexes for better performance
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_category_id ON transactions(category_id);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(date);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(type);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);

-- Composite index for common query patterns
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON transactions(user_id, date);
CREATE INDEX IF NOT EXISTS idx_transactions_user_type_date ON transactions(user_id, type, date);

-- Add sequence for auto-incrementing IDs (if not already exists)
CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

-- Insert default categories if they don't exist
INSERT INTO category (id, name, description) VALUES 
(1, 'Food & Dining', 'Restaurants, groceries, food delivery'),
(2, 'Transportation', 'Gas, public transport, car maintenance'),
(3, 'Shopping', 'Clothing, electronics, household items'),
(4, 'Entertainment', 'Movies, games, subscriptions'),
(5, 'Bills & Utilities', 'Rent, electricity, internet, phone'),
(6, 'Healthcare', 'Doctor visits, medications, insurance'),
(7, 'Education', 'Books, courses, tuition'),
(8, 'Salary', 'Monthly salary and wages'),
(9, 'Investments', 'Stocks, dividends, returns'),
(10, 'Other', 'Miscellaneous income and expenses')
ON CONFLICT (id) DO NOTHING;

-- Add comments for documentation
COMMENT ON TABLE transactions IS 'Financial transactions with audit trails';
COMMENT ON TABLE users IS 'User accounts with role-based access';
COMMENT ON TABLE category IS 'Transaction categories for classification';

COMMENT ON COLUMN transactions.amount IS 'Transaction amount in decimal format (precision 10, scale 2)';
COMMENT ON COLUMN transactions.created_at IS 'Timestamp when transaction was created';
COMMENT ON COLUMN transactions.updated_at IS 'Timestamp when transaction was last updated';
COMMENT ON COLUMN users.active IS 'Whether user account is active (true) or inactive (false)';
