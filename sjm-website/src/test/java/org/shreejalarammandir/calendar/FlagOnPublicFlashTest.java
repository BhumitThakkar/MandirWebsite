package org.shreejalarammandir.calendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.shreejalarammandir.support.SjmSpringTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@TestPropertySource(properties = "mandir.google-calendar.enabled=true")
class FlagOnPublicFlashTest extends SjmSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void flagOnSuccessShowsWithCalendar() throws Exception {
        when(googleCalendarClient.insert(any())).thenReturn("ok-evt");
        mockMvc.perform(post("/hall")
                        .param("eventTitle", "Wedding")
                        .param("reservationDate", "2026-12-01")
                        .param("startTime", "10:00")
                        .param("endTime", "14:00")
                        .param("paymentValidated", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("success",
                        "Your hall reservation was submitted and the calendar date has been booked."));
    }

    @Test
    void flagOnGoogleDownShowsCalendarFailed() throws Exception {
        when(googleCalendarClient.insert(any())).thenThrow(new RuntimeException("down"));
        mockMvc.perform(post("/hall")
                        .param("eventTitle", "Wedding")
                        .param("reservationDate", "2026-12-01")
                        .param("startTime", "10:00")
                        .param("endTime", "14:00")
                        .param("paymentValidated", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("success",
                        "Your hall reservation was submitted. Calendar booking failed. Please contact us."));
    }
}
