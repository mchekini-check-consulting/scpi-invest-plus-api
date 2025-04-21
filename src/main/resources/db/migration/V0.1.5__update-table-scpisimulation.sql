ALTER TABLE scpi_simulation
    ADD COLUMN stat_year DECIMAL(19, 2);

ALTER TABLE scpi_simulation
    ADD COLUMN gross_revenue DECIMAL(19, 2);

ALTER TABLE scpi_simulation
    ADD COLUMN net_revenue DECIMAL(19, 2);