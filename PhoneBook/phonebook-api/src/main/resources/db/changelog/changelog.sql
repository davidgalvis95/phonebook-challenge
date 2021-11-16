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

--changeset davidgalvis:5
DELETE FROM contacts WHERE first_name='arun' AND last_name='kart' AND phone_number=4158679089;
DELETE FROM contacts WHERE first_name='juan' AND last_name='torus' AND phone_number=3012390930;
DELETE FROM contacts WHERE first_name='nolux' AND last_name='fernandez' AND phone_number=3102930291;
INSERT INTO contacts(first_name, last_name, phone_number) VALUES ('arun','kart', 4158679089);
INSERT INTO contacts(first_name, last_name, phone_number) VALUES ('juan','torus', 3012390930);
INSERT INTO contacts(first_name, last_name, phone_number) VALUES ('nolux','fernandez', 3102930291);