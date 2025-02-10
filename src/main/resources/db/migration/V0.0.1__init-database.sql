CREATE TABLE investor
(
    last_name          varchar,
    first_name         varchar,
    date_of_birth      date,
    email              text PRIMARY KEY,
    annual_income      integer,
    phone_number       varchar(10),
    marital_status     varchar,
    number_of_children varchar
);

CREATE TABLE location
(
    country            varchar,
    country_percentage float,
    scpi_id            integer,
    PRIMARY KEY (country, scpi_id)
);

CREATE TABLE sector
(
    name              varchar,
<<<<<<< HEAD
    sector_percentage integer,
=======
    sector_percentage float,
>>>>>>> 344b63a5d5a1c7ba9acf5f62c4eef21340e2a4a4
    scpi_id           integer,
    PRIMARY KEY (name, scpi_id)
);

CREATE TABLE scpi
(
    id                   integer PRIMARY KEY,
    name                 varchar,
    minimum_subscription integer,
    manager              varchar,
<<<<<<< HEAD
    capitalization       numeric,
=======
    capitalization       BIGINT,
>>>>>>> 344b63a5d5a1c7ba9acf5f62c4eef21340e2a4a4
    subscription_fees    float,
    management_costs     float,
    enjoyment_delay      integer,
    iban                 varchar UNIQUE,
    bic                  varchar UNIQUE,
    scheduled_payment    bool,
<<<<<<< HEAD
    cashback             numeric,
=======
    cashback             float,
>>>>>>> 344b63a5d5a1c7ba9acf5f62c4eef21340e2a4a4
    advertising          text
);

CREATE TABLE stat_year
(
    year_stat            integer,
    distribution_rate    float,
    share_price          float,
    reconstitution_value float,
    scpi_id              integer,
    PRIMARY KEY (year_stat, scpi_id)
);

ALTER TABLE location
    ADD FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE;

ALTER TABLE sector
    ADD FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE;

ALTER TABLE stat_year
    ADD FOREIGN KEY (scpi_id) REFERENCES scpi (id) ON DELETE CASCADE;
