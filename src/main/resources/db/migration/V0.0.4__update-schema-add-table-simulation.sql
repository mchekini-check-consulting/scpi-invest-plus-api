CREATE TABLE simulation (
                            id               SERIAL PRIMARY KEY,
                            name             VARCHAR,
                            simulation_date  DATE,
                            investor_email      varchar NOT NULL,
                            CONSTRAINT fk_investor
                                FOREIGN KEY (investor_email)
                                    REFERENCES investor (email)
);

CREATE TABLE scpi_simulation (
                                 simulation_id INTEGER NOT NULL,
                                 scpi_id INTEGER NOT NULL,
                                 number_part INTEGER NOT NULL,
                                 part_price DECIMAL(19, 2),
                                 rising DECIMAL(19, 2),
                                 duree INTEGER,
                                 duree_percentage DECIMAL(5, 2),
                                 property_type VARCHAR,
                                 PRIMARY KEY (simulation_id, scpi_id),
                                 FOREIGN KEY (simulation_id) REFERENCES simulation(id) ON DELETE CASCADE,
                                 FOREIGN KEY (scpi_id) REFERENCES scpi(id) ON DELETE CASCADE
);
