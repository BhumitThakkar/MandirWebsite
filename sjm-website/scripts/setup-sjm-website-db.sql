-- Greenfield source of truth. Hibernate boots with ddl-auto=validate.
-- google_calendar_event_id lives in CREATE TABLE (not ALTER-only).

CREATE SCHEMA IF NOT EXISTS sjm;

CREATE TABLE IF NOT EXISTS sjm.app_property (
    property_key   VARCHAR(255) PRIMARY KEY,
    property_value VARCHAR(2048) NULL
);

CREATE TABLE IF NOT EXISTS sjm.hall_reservations (
    id                       BIGSERIAL PRIMARY KEY,
    public_id                VARCHAR(64)  NOT NULL UNIQUE,
    event_title              VARCHAR(255) NOT NULL,
    reservation_date         DATE         NOT NULL,
    start_time               TIME         NOT NULL,
    end_time                 TIME         NOT NULL,
    status                   VARCHAR(64)  NOT NULL,
    basement                 VARCHAR(16)  NULL,
    guest_count              INTEGER      NULL,
    nonprofit                BOOLEAN      NOT NULL DEFAULT FALSE,
    payment_validated        BOOLEAN      NULL,
    sjm_pujari_seva          BOOLEAN      NOT NULL DEFAULT FALSE,
    sjm_catering_seva        BOOLEAN      NOT NULL DEFAULT FALSE,
    admin_notes              VARCHAR(2048) NULL,
    contact_name             VARCHAR(255) NULL,
    contact_email            VARCHAR(255) NULL,
    contact_phone            VARCHAR(64)  NULL,
    google_calendar_event_id VARCHAR(1024) NULL,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sjm.hall_reservation_service_lines (
    id                   BIGSERIAL PRIMARY KEY,
    hall_reservation_id  BIGINT       NOT NULL REFERENCES sjm.hall_reservations (id) ON DELETE CASCADE,
    title                VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS sjm.catering (
    id                  BIGSERIAL PRIMARY KEY,
    hall_reservation_id BIGINT       NOT NULL UNIQUE REFERENCES sjm.hall_reservations (id) ON DELETE CASCADE,
    meal_summary        VARCHAR(1024) NULL
);

CREATE TABLE IF NOT EXISTS sjm.pujari_seva (
    id                       BIGSERIAL PRIMARY KEY,
    public_id                VARCHAR(64)  NOT NULL UNIQUE,
    seva_date                DATE         NOT NULL,
    start_time               TIME         NOT NULL,
    end_time                 TIME         NOT NULL,
    status                   VARCHAR(64)  NOT NULL,
    venue                    VARCHAR(255) NULL,
    selected_sevas           VARCHAR(1024) NULL,
    admin_notes              VARCHAR(2048) NULL,
    google_calendar_event_id VARCHAR(1024) NULL,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sjm.ledger_transactions (
    id                  BIGSERIAL PRIMARY KEY,
    hall_reservation_id BIGINT NULL REFERENCES sjm.hall_reservations (id),
    pujari_seva_id      BIGINT NULL REFERENCES sjm.pujari_seva (id),
    voided              BOOLEAN NOT NULL DEFAULT FALSE,
    amount_cents        BIGINT NULL
);
