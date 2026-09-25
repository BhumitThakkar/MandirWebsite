package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.shreejalarammandir.service.calendar.GoogleCalendarClient;
import org.shreejalarammandir.service.calendar.GoogleCalendarService;
import org.shreejalarammandir.service.calendar.NoOpGoogleCalendarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FlagOffContextLoadTest {

    @Autowired
    private GoogleCalendarClient googleCalendarClient;
    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Test
    void bootsWithNoOpClientWhenFlagOffAndNoMock() {
        assertThat(googleCalendarClient).isInstanceOf(NoOpGoogleCalendarClient.class);
        assertThat(googleCalendarService).isNotNull();
    }
}
