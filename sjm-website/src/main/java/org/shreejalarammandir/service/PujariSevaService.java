package org.shreejalarammandir.service;

import org.shreejalarammandir.dto.PujariSevaAdminForm;
import org.shreejalarammandir.dto.PujariSevaForm;
import org.shreejalarammandir.mapper.PujariSevaMapper;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.model.PujariSevaStatus;
import org.shreejalarammandir.repository.PujariSevaRepository;
import org.shreejalarammandir.service.calendar.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PujariSevaService {

    private static final Logger log = LoggerFactory.getLogger(PujariSevaService.class);

    private final PujariSevaRepository repository;
    private final PujariSevaMapper mapper;
    private final ReservationEmailService emailService;
    private final GoogleCalendarService googleCalendarService;
    private final LedgerGuard ledgerGuard;

    public PujariSevaService(
            PujariSevaRepository repository,
            PujariSevaMapper mapper,
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
    public PujariSeva submit(PujariSevaForm form) {
        PujariSeva entity = mapper.toEntity(form);
        entity.setPublicId(HallReservationService.newPublicId("pujari"));
        entity.setStatus(PujariSevaStatus.PENDING);
        PujariSeva saved = repository.save(entity);
        try {
            emailService.sendPujariSummary(saved);
        } catch (Exception e) {
            log.warn("Pujari email failed for publicId={}", saved.getPublicId(), e);
        }
        try {
            googleCalendarService.syncStandalonePujariCreated(saved);
        } catch (Exception e) {
            log.error("Pujari calendar create failed for publicId={}", saved.getPublicId(), e);
        }
        return saved;
    }

    @Transactional
    public PujariSeva adminUpdate(String publicId, PujariSevaAdminForm form) {
        PujariSeva entity = requireByPublicId(publicId);
        mapper.updateEntity(form, entity);
        PujariSeva saved = repository.save(entity);
        try {
            if (saved.getStatus() != null && saved.getStatus().isSoftCancelOnCalendar()) {
                googleCalendarService.syncStandalonePujariCancelled(saved);
            } else {
                googleCalendarService.syncStandalonePujariUpdated(saved);
            }
        } catch (Exception e) {
            log.error("Pujari calendar update failed for publicId={}", saved.getPublicId(), e);
        }
        return saved;
    }

    @Transactional
    public void deleteById(Long id) {
        PujariSeva entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown pujari seva id: " + id));
        ledgerGuard.assertDeletable(entity);
        try {
            googleCalendarService.syncStandalonePujariCancelled(entity);
        } catch (Exception e) {
            log.error("Pujari calendar cancel-before-delete failed for publicId={}", entity.getPublicId(), e);
        }
        repository.delete(entity);
    }

    public PujariSeva requireByPublicId(String publicId) {
        return repository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown pujari seva: " + publicId));
    }
}
