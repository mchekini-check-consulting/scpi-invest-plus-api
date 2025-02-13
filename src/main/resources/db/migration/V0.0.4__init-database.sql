CREATE TABLE investor
(
    last_name          VARCHAR,
    first_name         VARCHAR,
    date_of_birth      DATE,
    email              TEXT PRIMARY KEY,
    annual_income      INTEGER,
    phone_number       VARCHAR(10),
    marital_status     VARCHAR,
    number_of_children VARCHAR
);

CREATE TABLE location
(
    country            VARCHAR,
    country_percentage FLOAT,
    scpi_id            INTEGER,
    PRIMARY KEY (country, scpi_id),
    FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE
);

CREATE TABLE sector
(
    name               VARCHAR,
    sector_percentage  INTEGER,
    scpi_id            INTEGER,
    PRIMARY KEY (name, scpi_id),
    FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE
);

CREATE TABLE scpi
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR,
    minimum_subscription INTEGER,
    manager              VARCHAR,
    capitalization       NUMERIC,
    subscription_fees    FLOAT,
    management_costs     FLOAT,
    enjoyment_delay      INTEGER,
    iban                 VARCHAR UNIQUE,
    bic                  VARCHAR,
    scheduled_payment    BOOLEAN,
    cashback             NUMERIC,
    advertising          TEXT
);

CREATE TABLE stat_year
(
    year_stat            INTEGER,
    distribution_rate    FLOAT,
    share_price          FLOAT,
    reconstitution_value FLOAT,
    scpi_id              INTEGER,
    PRIMARY KEY (year_stat, scpi_id),
    FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE
);
