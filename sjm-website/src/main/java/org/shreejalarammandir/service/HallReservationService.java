package org.shreejalarammandir.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.shreejalarammandir.dto.HallReservationAdminForm;
import org.shreejalarammandir.dto.HallReservationForm;
import org.shreejalarammandir.dto.SubmitReservationResult;
import org.shreejalarammandir.mapper.HallReservationMapper;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.HallReservationServiceLine;
import org.shreejalarammandir.model.ReservationStatus;
import org.shreejalarammandir.repository.HallReservationRepository;
import org.shreejalarammandir.service.calendar.CalendarSyncOutcome;
import org.shreejalarammandir.service.calendar.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HallReservationService {

    private static final Logger log = LoggerFactory.getLogger(HallReservationService.class);

    private final HallReservationRepository repository;
    private final HallReservationMapper mapper;
    private final ReservationEmailService emailService;
    private final GoogleCalendarService googleCalendarService;
    private final LedgerGuard ledgerGuard;

    public HallReservationService(
            HallReservationRepository repository,
            HallReservationMapper mapper,
            ReservationEmailService emailService,
            GoogleCalendarService googleCalendarService,
            LedgerGuard ledgerGuard) {
        this.repository = repository;
        this.mapper = mapper;
        this.emailService = emailService;
        this.googleCalendarService = googleCalendarService;
        this.ledgerGuard = ledgerGuard;
    }

    @Transactional
    public SubmitReservationResult submitReservation(HallReservationForm form) {
        HallReservation entity = mapper.toEntity(form);
        entity.setPublicId(newPublicId("hall"));
        entity.setPaymentValidated(form.isPaymentValidated());
        entity.setStatus(initialStatus(form));
        entity.replaceServiceLines(parseLines(form.getPujariLineTitles()));
        HallReservation saved = repository.save(entity);
        try {
            emailService.sendReservationSummary(saved);
        } catch (Exception e) {
            log.warn("Hall reservation email failed for publicId={}", saved.getPublicId(), e);
        }
        CalendarSyncOutcome calendarOutcome;
        try {
            calendarOutcome = googleCalendarService.syncHallCreated(saved);
        } catch (Exception e) {
            log.error("Hall calendar create failed for publicId={}", saved.getPublicId(), e);
            calendarOutcome = CalendarSyncOutcome.failed();
        }
        return new SubmitReservationResult(saved, calendarOutcome);
    }

    @Transactional
    public HallReservation adminUpdate(String publicId, HallReservationAdminForm form) {
        HallReservation entity = requireByPublicId(publicId);
        mapper.updateEntity(form, entity);
        entity.replaceServiceLines(parseLines(form.getPujariLineTitles()));
        HallReservation saved = repository.save(entity);
        try {
            if (saved.getStatus() != null && saved.getStatus().isSoftCancelOnCalendar()) {
                googleCalendarService.syncHallCancelled(saved);
            } else {
                googleCalendarService.syncHallUpdated(saved);
            }
        } catch (Exception e) {
            log.error("Hall calendar update failed for publicId={}", saved.getPublicId(), e);
        }
        return saved;
    }

    /**
     * Used by catering-approve. Do not hook calendar here — catering method covers it.
     */
    @Transactional
    public HallReservation save(HallReservation reservation) {
        return repository.save(reservation);
    }

    @Transactional
    public void delete(String publicId) {
        HallReservation entity = requireByPublicId(publicId);
        ledgerGuard.assertDeletable(entity);
        try {
            googleCalendarService.syncHallCancelled(entity);
        } catch (Exception e) {
            log.error("Hall calendar cancel-before-delete failed for publicId={}", entity.getPublicId(), e);
        }
        repository.delete(entity);
    }

    public HallReservation requireByPublicId(String publicId) {
        return repository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown hall reservation: " + publicId));
    }

    private static ReservationStatus initialStatus(HallReservationForm form) {
        if (form.isPaymentValidated()) {
            return form.isSjmCateringSeva()
                    ? ReservationStatus.SJM_CATERING_PENDING
                    : ReservationStatus.APPROVED;
        }
        return ReservationStatus.PAYMENT_VALIDATION_PENDING;
    }

    private static List<HallReservationServiceLine> parseLines(String raw) {
        List<HallReservationServiceLine> lines = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return lines;
        }
        for (String part : raw.split(",")) {
            String title = part.trim();
            if (!title.isEmpty()) {
                lines.add(new HallReservationServiceLine(title));
            }
        }
        return lines;
    }

    static String newPublicId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }
}
