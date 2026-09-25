package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.shreejalarammandir.model.PujariSeva;

import jakarta.persistence.Table;

class LiveContractSourceTest {

    private static final Path MAIN_JAVA = Path.of("src/main/java/org/shreejalarammandir");

    @Test
    void hallControllerDoesNotCallPlaceholderAfterSubmit() throws Exception {
        String src = Files.readString(MAIN_JAVA.resolve("controller/HallReservationController.java"));
        assertThat(src).contains("submitReservation");
        assertThat(src).doesNotContain("createCalendarEvent");
        assertThat(src).doesNotContain("GoogleCalendarService");
        assertThat(src).contains("CalendarSyncOutcome");
    }

    @Test
    void googleCalendarServiceHasNoPlaceholderMethodAndNoFakeCalPrefix() throws Exception {
        String src = Files.readString(MAIN_JAVA.resolve("service/calendar/GoogleCalendarService.java"));
        assertThat(src).doesNotContain("createCalendarEvent(");
        assertThat(src).doesNotContain("CAL_");
        assertThat(src).contains("syncHallCreated");
        assertThat(src).contains("syncHallContentRefresh");
        assertThat(src).contains("syncStandalonePujariCreated");
    }

    @Test
    void onlyOneGoogleCalendarServiceWriter() throws Exception {
        try (Stream<Path> files = Files.walk(MAIN_JAVA)) {
            long writers = files
                    .filter(path -> path.getFileName().toString().equals("GoogleCalendarService.java"))
                    .count();
            assertThat(writers).isEqualTo(1);
        }
    }

    @Test
    void cateringControllerIsTheApproveCallerAndSaveIsUnhooked() throws Exception {
        String cateringController = Files.readString(MAIN_JAVA.resolve("controller/CateringController.java"));
        assertThat(cateringController).contains("saveCateringAndApproveReservation");
        assertThat(cateringController).doesNotContain("cateringService.save(");

        String allMain = readAllJava(MAIN_JAVA);
        assertThat(countOccurrences(allMain, "saveCateringAndApproveReservation(")).isGreaterThanOrEqualTo(2);
        assertThat(countOccurrences(allMain, "cateringService.save(")).isZero();
    }

    @Test
    void pujariEntityTableIsSingular() {
        Table table = PujariSeva.class.getAnnotation(Table.class);
        assertThat(table).isNotNull();
        assertThat(table.name()).isEqualTo("pujari_seva");
        assertThat(table.schema()).isEqualTo("sjm");
    }

    private static String readAllJava(Path root) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Stream<Path> files = Files.walk(root)) {
            for (Path path : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                sb.append(Files.readString(path)).append('\n');
            }
        }
        return sb.toString();
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0;
        int from = 0;
        while (true) {
            int at = haystack.indexOf(needle, from);
            if (at < 0) {
                return count;
            }
            count++;
            from = at + needle.length();
        }
    }
}
