CREATE TABLE tax_bracket (
                             id SERIAL PRIMARY KEY,
                             lower_bound DECIMAL(10,2) NOT NULL,
                             upper_bound DECIMAL(10,2),
                             tax_rate DECIMAL(5,2) NOT NULL
);
INSERT INTO tax_bracket (lower_bound, upper_bound, tax_rate) VALUES
                                                                 (0.00, 11294.00, 0.00),
                                                                 (11294.00, 28797.00, 11.00),
                                                                 (28797.00, 82341.00, 30.00),
                                                                 (82341.00, 177105.00, 41.00),
                                                                 (177105.00, NULL, 45.00);
