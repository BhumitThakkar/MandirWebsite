package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.shreejalarammandir.dto.HallReservationForm;
import org.shreejalarammandir.dto.SubmitReservationResult;
import org.shreejalarammandir.model.BasementOccupancy;
import org.shreejalarammandir.service.HallReservationService;
import org.shreejalarammandir.support.SjmSpringTest;
import org.shreejalarammandir.web.HallReservationMessageKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

class FlagOffCalendarTest extends SjmSpringTest {

    @Autowired
    private HallReservationService hallReservationService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void flagOffDoesNotCallClientAndFlashIsNotCalendarFailed() throws Exception {
        HallReservationForm form = new HallReservationForm();
        form.setEventTitle("Anniversary");
        form.setReservationDate(LocalDate.of(2026, 11, 2));
        form.setStartTime(LocalTime.of(12, 0));
        form.setEndTime(LocalTime.of(15, 0));
        form.setBasement(BasementOccupancy.HALF);
        form.setPaymentValidated(true);

        SubmitReservationResult result = hallReservationService.submitReservation(form);
        assertThat(result.getCalendarOutcome().isNotAttempted()).isTrue();
        assertThat(result.getReservation().getGoogleCalendarEventId()).isNull();
        verifyNoInteractions(googleCalendarClient);

        mockMvc.perform(post("/hall")
                        .param("eventTitle", "Anniversary")
                        .param("reservationDate", "2026-11-02")
                        .param("startTime", "12:00")
                        .param("endTime", "15:00")
                        .param("basement", "HALF")
                        .param("paymentValidated", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/hall"))
                .andExpect(flash().attribute("success",
                        org.hamcrest.Matchers.allOf(
                                org.hamcrest.Matchers.not(
                                        org.hamcrest.Matchers.containsString("Calendar booking failed")),
                                org.hamcrest.Matchers.not(
                                        org.hamcrest.Matchers.equalTo(
                                                "Your hall reservation was submitted. Calendar booking failed. Please contact us.")))));
    }

    @Test
    void publicFlashKeyForFlagOffIsApprovedNotFailed() throws Exception {
        mockMvc.perform(post("/hall")
                        .param("eventTitle", "Anniversary")
                        .param("reservationDate", "2026-11-02")
                        .param("startTime", "12:00")
                        .param("endTime", "15:00")
                        .param("basement", "FULL")
                        .param("paymentValidated", "true"))
                .andExpect(flash().attribute("success",
                        "Your hall reservation was submitted and is approved."));
        assertThat(HallReservationMessageKeys.SUCCESS_APPROVED_CALENDAR_FAILED)
                .isNotEqualTo("Flash.hallReservation.success.approved");
        verifyNoInteractions(googleCalendarClient);
    }
}
