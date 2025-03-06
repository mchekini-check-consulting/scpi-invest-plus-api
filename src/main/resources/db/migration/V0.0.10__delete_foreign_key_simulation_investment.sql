ALTER TABLE simulation
    DROP CONSTRAINT fk_investor;

ALTER TABLE investment
    DROP CONSTRAINT investment_investor_id_fkey;