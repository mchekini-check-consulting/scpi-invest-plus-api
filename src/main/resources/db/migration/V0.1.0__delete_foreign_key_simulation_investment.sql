ALTER TABLE simulation
    DROP CONSTRAINT IF EXISTS fk_investor;

ALTER TABLE investment
    DROP CONSTRAINT IF EXISTS investment_investor_id_fkey;


