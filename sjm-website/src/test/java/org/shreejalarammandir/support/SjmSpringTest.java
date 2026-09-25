package org.shreejalarammandir.support;

import org.shreejalarammandir.service.calendar.GoogleCalendarClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class SjmSpringTest {

    @MockBean
    protected GoogleCalendarClient googleCalendarClient;
}
