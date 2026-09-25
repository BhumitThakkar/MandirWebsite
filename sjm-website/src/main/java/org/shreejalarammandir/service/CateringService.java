package org.shreejalarammandir.service;

import org.shreejalarammandir.dto.CateringForm;
import org.shreejalarammandir.model.Catering;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.ReservationStatus;
import org.shreejalarammandir.repository.CateringRepository;
import org.shreejalarammandir.service.calendar.GoogleCalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CateringService {

    private static final Logger log = LoggerFactory.getLogger(CateringService.class);

    private final CateringRepository cateringRepository;
    private final HallReservationService hallReservationService;
    private final GoogleCalendarService googleCalendarService;

    public CateringService(
            CateringRepository cateringRepository,
            HallReservationService hallReservationService,
            GoogleCalendarService googleCalendarService) {
        this.cateringRepository = cateringRepository;
        this.hallReservationService = hallReservationService;
        this.googleCalendarService = googleCalendarService;
    }

    /**
     * Unused by admin UI today. Do not hook calendar here.
     */
    @Transactional
    public Catering save(Catering catering) {
        return cateringRepository.save(catering);
    }

    @Transactional
    public Catering saveCateringAndApproveReservation(String hallPublicId, CateringForm form) {
        HallReservation hall = hallReservationService.requireByPublicId(hallPublicId);
        Catering catering = cateringRepository.findByHallReservationId(hall.getId()).orElseGet(Catering::new);
        catering.setHallReservation(hall);
        catering.setMealSummary(form.getMealSummary());
        Catering savedCatering = cateringRepository.save(catering);
        hall.setStatus(ReservationStatus.APPROVED);
        hall.setSjmCateringSeva(true);
        hallReservationService.save(hall);
        try {
            googleCalendarService.syncHallContentRefresh(hall);
        } catch (Exception e) {
            log.error("Catering calendar refresh failed for hall publicId={}", hallPublicId, e);
        }
        return savedCatering;
    }

    @Transactional
    public void deleteByHallReservationId(String hallPublicId) {
        HallReservation hall = hallReservationService.requireByPublicId(hallPublicId);
        cateringRepository.deleteByHallReservationId(hall.getId());
        hall.setSjmCateringSeva(false);
        hallReservationService.save(hall);
        try {
            googleCalendarService.syncHallContentRefresh(hall);
        } catch (Exception e) {
            log.error("Catering-delete calendar refresh failed for hall publicId={}", hallPublicId, e);
        }
    }
}
