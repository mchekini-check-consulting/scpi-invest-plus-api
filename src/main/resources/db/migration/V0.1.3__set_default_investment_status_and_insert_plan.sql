ALTER TABLE IF EXISTS investment
    ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'STANDARD';