CREATE TABLE plan
(
    id             SERIAL PRIMARY KEY,
    functionality  VARCHAR(255) NOT NULL,
    standard       BOOLEAN,
    premium        BOOLEAN,
    description    TEXT
);


INSERT INTO plan (functionality, standard, premium, description)
VALUES ('list-scpi', TRUE, TRUE, 'Récupération des informations des SCPI Partenaires'),
       ('my-investments', TRUE, TRUE, 'Suivi en temps réel de mes investissements'),
       ('simulation', FALSE, TRUE, 'Simulation avancée des investissements')
