DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relname = 'scpi_id_seq') THEN
        CREATE SEQUENCE scpi_id_seq OWNED BY scpi.id;
    END IF;
END $$;

ALTER TABLE scpi ALTER COLUMN id SET DEFAULT nextval('scpi_id_seq');

ALTER TABLE scpi DROP CONSTRAINT scpi_bic_key;



