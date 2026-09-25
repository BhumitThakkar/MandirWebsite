package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class DdlScriptTest {

    @Test
    void createTableBodiesIncludeCalendarEventIdAndSingularPujariTable() throws Exception {
        Path setup = Path.of("scripts/setup-sjm-website-db.sql");
        String sql = Files.readString(setup);

        assertThat(sql).contains("CREATE TABLE IF NOT EXISTS sjm.hall_reservations");
        assertThat(sql).contains("CREATE TABLE IF NOT EXISTS sjm.pujari_seva");
        assertThat(sql).doesNotContain("pujari_sevas");

        int hallCreate = sql.indexOf("CREATE TABLE IF NOT EXISTS sjm.hall_reservations");
        int hallEnd = sql.indexOf(");", hallCreate);
        String hallBody = sql.substring(hallCreate, hallEnd);
        assertThat(hallBody).contains("google_calendar_event_id VARCHAR(1024) NULL");

        int pujariCreate = sql.indexOf("CREATE TABLE IF NOT EXISTS sjm.pujari_seva");
        int pujariEnd = sql.indexOf(");", pujariCreate);
        String pujariBody = sql.substring(pujariCreate, pujariEnd);
        assertThat(pujariBody).contains("google_calendar_event_id VARCHAR(1024) NULL");
    }

    @Test
    void optionalAlterTargetsSingularPujariTable() throws Exception {
        String sql = Files.readString(Path.of("scripts/alter-google-calendar-event-id.sql"));
        assertThat(sql).contains("ALTER TABLE sjm.hall_reservations");
        assertThat(sql).contains("ALTER TABLE sjm.pujari_seva");
        assertThat(sql).doesNotContain("pujari_sevas");
    }
}
