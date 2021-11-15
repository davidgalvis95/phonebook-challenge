--liquibase formatted sql

--changeset davidgalvis:1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--changeset davidgalvis:2
CREATE TABLE IF NOT EXISTS contacts
(
    id uuid DEFAULT uuid_generate_v4() NOT NULL,
    first_name character varying(15) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(15) COLLATE pg_catalog."default",
    phone_number bigint NOT NULL,
    CONSTRAINT contacts_pk PRIMARY KEY (id),
    CONSTRAINT valid_phone_number CHECK (phone_number <= 999999999999)
);

--changeset davidgalvis:4
DELETE FROM contacts WHERE first_name='Arun' AND last_name='Kart' AND phone_number=4158679089;
DELETE FROM contacts WHERE first_name='Juan' AND last_name='Torus' AND phone_number=3012390930;
DELETE FROM contacts WHERE first_name='Nolux' AND last_name='Fernandez' AND phone_number=3102930291;
INSERT INTO contacts(first_name, last_name, phone_number) VALUES ('Arun','Kart', 4158679089);
INSERT INTO contacts(first_name, last_name, phone_number) VALUES ('Juan','Torus', 3012390930);
INSERT INTO contacts(first_name, last_name, phone_number) VALUES ('Nolux','Fernandez', 3102930291);