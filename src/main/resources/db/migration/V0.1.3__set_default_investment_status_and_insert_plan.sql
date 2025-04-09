ALTER TABLE IF EXISTS investment
    ADD COLUMN IF NOT EXISTS status VARCHAR(50) DEFAULT 'STANDARD';

INSERT INTO plan (functionality, "standard", premium, description)
VALUES ('scheduled-payment', FALSE, TRUE, 'scheduled payment');