package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shreejalarammandir.controller.HallReservationController;
import org.shreejalarammandir.dto.SubmitReservationResult;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.service.calendar.CalendarSyncOutcome;
import org.shreejalarammandir.web.HallReservationMessageKeys;
import org.shreejalarammandir.web.ValidationMessageResolver;

@ExtendWith(MockitoExtension.class)
class HallReservationControllerFlashTest {

    @Mock
    private ValidationMessageResolver resolver;

    private HallReservationController controller;

    @BeforeEach
    void setUp() {
        when(resolver.resolveKey(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        controller = new HallReservationController(null, resolver);
    }

    @Test
    void flagOffIsNotCalendarFailed() {
        String flash = controller.flashFor(result(true, CalendarSyncOutcome.notAttempted()));
        assertThat(flash).isEqualTo(HallReservationMessageKeys.SUCCESS_APPROVED);
        assertThat(flash).doesNotContain("CalendarFailed");
        assertThat(flash).isNotEqualTo(HallReservationMessageKeys.SUCCESS_APPROVED_CALENDAR_FAILED);
        assertThat(flash).isNotEqualTo(HallReservationMessageKeys.SUCCESS_PENDING_CALENDAR_FAILED);
    }

    @Test
    void flagOnSuccessUsesWithCalendar() {
        assertThat(controller.flashFor(result(false, CalendarSyncOutcome.success("evt"))))
                .isEqualTo(HallReservationMessageKeys.SUCCESS_PENDING_WITH_CALENDAR);
        assertThat(controller.flashFor(result(true, CalendarSyncOutcome.success("evt"))))
                .isEqualTo(HallReservationMessageKeys.SUCCESS_APPROVED_WITH_CALENDAR);
    }

    @Test
    void flagOnFailureUsesCalendarFailed() {
        assertThat(controller.flashFor(result(true, CalendarSyncOutcome.failed())))
                .isEqualTo(HallReservationMessageKeys.SUCCESS_APPROVED_CALENDAR_FAILED);
    }

    private static SubmitReservationResult result(boolean paymentValidated, CalendarSyncOutcome outcome) {
        HallReservation reservation = new HallReservation();
        reservation.setPaymentValidated(paymentValidated);
        return new SubmitReservationResult(reservation, outcome);
    }
}
