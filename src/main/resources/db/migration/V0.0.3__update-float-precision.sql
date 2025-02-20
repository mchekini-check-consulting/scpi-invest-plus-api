ALTER TABLE scpi ALTER COLUMN subscription_fees TYPE NUMERIC(10,2);
ALTER TABLE scpi ALTER COLUMN management_costs TYPE NUMERIC(10,2);
ALTER TABLE location ALTER COLUMN country_percentage TYPE NUMERIC(10,2);
ALTER TABLE sector ALTER COLUMN sector_percentage TYPE NUMERIC(10,2);
ALTER TABLE stat_year ALTER COLUMN distribution_rate TYPE NUMERIC(10,2);
ALTER TABLE stat_year ALTER COLUMN share_price TYPE NUMERIC(10,2);
ALTER TABLE stat_year ALTER COLUMN reconstitution_value TYPE NUMERIC(10,2);