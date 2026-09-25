-- Optional one-shot for an already-existing DB that cannot re-run setup-sjm-website-db.sql.
-- Greenfield installs must use the CREATE TABLE bodies (validate mode).

ALTER TABLE sjm.hall_reservations
  ADD COLUMN IF NOT EXISTS google_calendar_event_id VARCHAR(1024) NULL;

ALTER TABLE sjm.pujari_seva
  ADD COLUMN IF NOT EXISTS google_calendar_event_id VARCHAR(1024) NULL;
