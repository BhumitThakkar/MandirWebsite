package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.shreejalarammandir.dto.CateringForm;
import org.shreejalarammandir.dto.HallReservationAdminForm;
import org.shreejalarammandir.dto.HallReservationForm;
import org.shreejalarammandir.dto.PujariSevaAdminForm;
import org.shreejalarammandir.dto.PujariSevaForm;
import org.shreejalarammandir.dto.SubmitReservationResult;
import org.shreejalarammandir.model.BasementOccupancy;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.LedgerTransaction;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.model.PujariSevaStatus;
import org.shreejalarammandir.model.ReservationStatus;
import org.shreejalarammandir.repository.HallReservationRepository;
import org.shreejalarammandir.repository.LedgerTransactionRepository;
import org.shreejalarammandir.repository.PujariSevaRepository;
import org.shreejalarammandir.service.CateringService;
import org.shreejalarammandir.service.HallReservationService;
import org.shreejalarammandir.service.NotDeletableException;
import org.shreejalarammandir.service.PujariSevaService;
import org.shreejalarammandir.service.calendar.GoogleCalendarEventRequest;
import org.shreejalarammandir.support.SjmSpringTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "mandir.google-calendar.enabled=true")
class GoogleCalendarSyncIntegrationTest extends SjmSpringTest {

    @Autowired
    private HallReservationService hallReservationService;
    @Autowired
    private CateringService cateringService;
    @Autowired
    private PujariSevaService pujariSevaService;
    @Autowired
    private HallReservationRepository hallReservationRepository;
    @Autowired
    private PujariSevaRepository pujariSevaRepository;
    @Autowired
    private LedgerTransactionRepository ledgerTransactionRepository;

    @Test
    void hallCreateStoresRealIdAndNeverAPlaceholderCalId() {
        AtomicInteger inserts = new AtomicInteger();
        when(googleCalendarClient.insert(any())).thenAnswer(invocation -> {
            inserts.incrementAndGet();
            return "google-hall-1";
        });

        SubmitReservationResult result = hallReservationService.submitReservation(hallForm(true, true));
        HallReservation stored = hallReservationRepository.findByPublicId(result.getReservation().getPublicId()).orElseThrow();

        assertThat(result.getCalendarOutcome().isSuccess()).isTrue();
        assertThat(stored.getGoogleCalendarEventId()).isEqualTo("google-hall-1");
        assertThat(stored.getGoogleCalendarEventId()).doesNotStartWith("CAL_");
        assertThat(stored.getStatus()).isEqualTo(ReservationStatus.SJM_CATERING_PENDING);
        assertThat(inserts.get()).isEqualTo(1);

        ArgumentCaptor<GoogleCalendarEventRequest> captor = ArgumentCaptor.forClass(GoogleCalendarEventRequest.class);
        verify(googleCalendarClient).insert(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("confirmed");
        assertThat(captor.getValue().getSummary()).contains("Wedding");
        assertThat(captor.getValue().getDescription()).contains("status: SJM_CATERING_PENDING");
        assertThat(captor.getValue().getDescription()).doesNotContain("$");
    }

    @Test
    void hallEditTitlePatchesSameEventId() {
        when(googleCalendarClient.insert(any())).thenReturn("edit-evt");
        HallReservation hall = hallReservationService.submitReservation(hallForm(true, false)).getReservation();

        HallReservationAdminForm form = adminForm(ReservationStatus.APPROVED, hall);
        form.setEventTitle("Wedding reception");
        hallReservationService.adminUpdate(hall.getPublicId(), form);

        HallReservation stored = hallReservationRepository.findByPublicId(hall.getPublicId()).orElseThrow();
        assertThat(stored.getGoogleCalendarEventId()).isEqualTo("edit-evt");
        ArgumentCaptor<GoogleCalendarEventRequest> captor = ArgumentCaptor.forClass(GoogleCalendarEventRequest.class);
        verify(googleCalendarClient).patch(eq("edit-evt"), captor.capture());
        assertThat(captor.getValue().getSummary()).contains("Wedding reception");
        verify(googleCalendarClient, times(1)).insert(any());
    }

    @Test
    void cateringPendingKeepsEventActive() {
        when(googleCalendarClient.insert(any())).thenReturn("pending-evt");
        HallReservation stored = hallReservationService.submitReservation(hallForm(true, true)).getReservation();
        stored = hallReservationRepository.findByPublicId(stored.getPublicId()).orElseThrow();
        assertThat(stored.getStatus()).isEqualTo(ReservationStatus.SJM_CATERING_PENDING);
        ArgumentCaptor<GoogleCalendarEventRequest> captor = ArgumentCaptor.forClass(GoogleCalendarEventRequest.class);
        verify(googleCalendarClient).insert(captor.capture());
        assertThat(captor.getValue().getStatus()).isNotEqualTo("cancelled");
        verify(googleCalendarClient, never()).cancel(any());
    }

    @Test
    void cateringApprovePatchesSameEventAndAddsMeals() {
        when(googleCalendarClient.insert(any())).thenReturn("shared-evt");
        HallReservation hall = hallReservationService.submitReservation(hallForm(true, true)).getReservation();

        CateringForm cateringForm = new CateringForm();
        cateringForm.setMealSummary("Dinner thali");
        cateringService.saveCateringAndApproveReservation(hall.getPublicId(), cateringForm);

        HallReservation updated = hallReservationRepository.findByPublicId(hall.getPublicId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(ReservationStatus.APPROVED);
        assertThat(updated.getGoogleCalendarEventId()).isEqualTo("shared-evt");
        verify(googleCalendarClient, times(1)).insert(any());
        ArgumentCaptor<GoogleCalendarEventRequest> captor = ArgumentCaptor.forClass(GoogleCalendarEventRequest.class);
        verify(googleCalendarClient).patch(eq("shared-evt"), captor.capture());
        assertThat(captor.getValue().getDescription()).contains("Dinner thali");
        assertThat(captor.getValue().getDescription()).contains("status: APPROVED");
        assertThat(captor.getValue().getStatus()).isEqualTo("confirmed");
    }

    @Test
    void cancelThenReopenPatchesConfirmedWithoutSecondInsert() {
        when(googleCalendarClient.insert(any())).thenReturn("stable-evt");
        HallReservation hall = hallReservationService.submitReservation(hallForm(true, false)).getReservation();

        hallReservationService.adminUpdate(hall.getPublicId(), adminForm(ReservationStatus.CANCELLED, hall));
        hallReservationService.adminUpdate(hall.getPublicId(), adminForm(ReservationStatus.APPROVED, hall));

        HallReservation stored = hallReservationRepository.findByPublicId(hall.getPublicId()).orElseThrow();
        assertThat(stored.getStatus()).isEqualTo(ReservationStatus.APPROVED);
        assertThat(stored.getGoogleCalendarEventId()).isEqualTo("stable-evt");
        verify(googleCalendarClient, times(1)).insert(any());

        ArgumentCaptor<GoogleCalendarEventRequest> captor = ArgumentCaptor.forClass(GoogleCalendarEventRequest.class);
        verify(googleCalendarClient, times(2)).patch(eq("stable-evt"), captor.capture());
        assertThat(captor.getAllValues().get(0).getStatus()).isEqualTo("cancelled");
        assertThat(captor.getAllValues().get(0).getSummary()).contains("Wedding");
        assertThat(captor.getAllValues().get(1).getStatus()).isEqualTo("confirmed");
    }

    @Test
    void hallDeleteSoftCancelsThenRemovesRowWhenR4Allows() {
        when(googleCalendarClient.insert(any())).thenReturn("to-delete");
        HallReservation hall = hallReservationService.submitReservation(hallForm(false, false)).getReservation();
        String publicId = hall.getPublicId();

        hallReservationService.delete(publicId);

        verify(googleCalendarClient).patch(eq("to-delete"), any());
        assertThat(hallReservationRepository.findByPublicId(publicId)).isEmpty();
    }

    @Test
    void hallDeleteBlockedByR4DoesNotRemoveRow() {
        when(googleCalendarClient.insert(any())).thenReturn("blocked");
        HallReservation hall = hallReservationService.submitReservation(hallForm(false, false)).getReservation();
        LedgerTransaction tx = new LedgerTransaction();
        tx.setHallReservationId(hall.getId());
        tx.setVoided(false);
        ledgerTransactionRepository.save(tx);

        assertThatThrownBy(() -> hallReservationService.delete(hall.getPublicId()))
                .isInstanceOf(NotDeletableException.class);
        assertThat(hallReservationRepository.findByPublicId(hall.getPublicId())).isPresent();
    }

    @Test
    void standalonePujariCreateUpdateDelete() {
        when(googleCalendarClient.insert(any())).thenReturn("pujari-evt");
        PujariSeva seva = pujariSevaService.submit(pujariForm());
        PujariSeva stored = pujariSevaRepository.findByPublicId(seva.getPublicId()).orElseThrow();
        assertThat(stored.getGoogleCalendarEventId()).isEqualTo("pujari-evt");
        assertThat(stored.getGoogleCalendarEventId()).doesNotStartWith("CAL_");

        PujariSevaAdminForm update = new PujariSevaAdminForm();
        update.setSevaDate(stored.getSevaDate());
        update.setStartTime(stored.getStartTime());
        update.setEndTime(stored.getEndTime());
        update.setSelectedSevas(stored.getSelectedSevas());
        update.setStatus(PujariSevaStatus.CANCELLED);
        pujariSevaService.adminUpdate(stored.getPublicId(), update);
        verify(googleCalendarClient).patch(eq("pujari-evt"), any());

        pujariSevaService.deleteById(stored.getId());
        assertThat(pujariSevaRepository.findByPublicId(stored.getPublicId())).isEmpty();
    }

    @Test
    void calendarFailureDoesNotFailBooking() {
        when(googleCalendarClient.insert(any())).thenThrow(new RuntimeException("Google down"));
        SubmitReservationResult result = hallReservationService.submitReservation(hallForm(true, false));
        assertThat(result.getReservation().getId()).isNotNull();
        assertThat(result.getCalendarOutcome().isFailed()).isTrue();
        HallReservation stored = hallReservationRepository.findByPublicId(result.getReservation().getPublicId()).orElseThrow();
        assertThat(stored.getGoogleCalendarEventId()).isNull();
    }

    @Test
    void cateringSaveWithoutApproveIsNotTheHook() {
        when(googleCalendarClient.insert(any())).thenReturn("evt");
        HallReservation hall = hallReservationService.submitReservation(hallForm(true, true)).getReservation();
        org.shreejalarammandir.model.Catering catering = new org.shreejalarammandir.model.Catering();
        catering.setHallReservation(hall);
        catering.setMealSummary("Should not sync");
        cateringService.save(catering);
        verify(googleCalendarClient, times(1)).insert(any());
        verify(googleCalendarClient, never()).patch(any(), any());
    }

    private HallReservationForm hallForm(boolean paymentValidated, boolean catering) {
        HallReservationForm form = new HallReservationForm();
        form.setEventTitle("Wedding");
        form.setReservationDate(LocalDate.of(2026, 10, 12));
        form.setStartTime(LocalTime.of(10, 0));
        form.setEndTime(LocalTime.of(16, 0));
        form.setBasement(BasementOccupancy.FULL);
        form.setGuestCount(120);
        form.setNonprofit(false);
        form.setPaymentValidated(paymentValidated);
        form.setSjmCateringSeva(catering);
        form.setSjmPujariSeva(true);
        form.setPujariLineTitles("Ganesh puja");
        form.setContactEmail("hidden@example.com");
        form.setGoogleCalendarEventId("CAL_should_be_ignored");
        return form;
    }

    private HallReservationAdminForm adminForm(ReservationStatus status, HallReservation hall) {
        HallReservationAdminForm form = new HallReservationAdminForm();
        form.setEventTitle(hall.getEventTitle());
        form.setReservationDate(hall.getReservationDate());
        form.setStartTime(hall.getStartTime());
        form.setEndTime(hall.getEndTime());
        form.setStatus(status);
        form.setBasement(hall.getBasement());
        form.setGuestCount(hall.getGuestCount());
        form.setSjmPujariSeva(hall.isSjmPujariSeva());
        form.setPujariLineTitles("Ganesh puja");
        form.setGoogleCalendarEventId("CAL_ignored_on_update");
        return form;
    }

    private PujariSevaForm pujariForm() {
        PujariSevaForm form = new PujariSevaForm();
        form.setSevaDate(LocalDate.of(2026, 10, 18));
        form.setStartTime(LocalTime.of(9, 0));
        form.setEndTime(LocalTime.of(10, 30));
        form.setSelectedSevas("Satyanarayan katha");
        form.setVenue("Mandir");
        form.setGoogleCalendarEventId("CAL_pujari_ignored");
        return form;
    }
}
