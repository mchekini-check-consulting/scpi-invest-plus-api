CREATE TABLE ref_dismemberment
(
    id                   SERIAL PRIMARY KEY,
    property_type        varchar,
    year_dismemberment    integer,
    rate_dismemberment NUMERIC(10,2)
);

CREATE TABLE investment
(
    id  SERIAL PRIMARY KEY,
    type_property varchar,
    number_shares integer,
    number_years integer,
    total_amount NUMERIC(10,2),
    Investment_state varchar,
    investor_id text,
    scpi_id      integer
);

ALTER TABLE investment
    ADD FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE;
ALTER TABLE investment
    ADD FOREIGN KEY (investor_id) REFERENCES investor (email) ON DELETE CASCADE;