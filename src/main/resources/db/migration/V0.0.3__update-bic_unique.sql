CREATE SEQUENCE IF NOT EXISTS scpi_id_seq;
ALTER TABLE scpi ALTER COLUMN id SET DEFAULT nextval('scpi_id_seq');

ALTER TABLE scpi DROP CONSTRAINT scpi_bic_key;



