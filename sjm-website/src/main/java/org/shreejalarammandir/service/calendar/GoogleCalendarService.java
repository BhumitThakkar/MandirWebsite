package org.shreejalarammandir.service.calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.shreejalarammandir.config.GoogleCalendarProperties;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.repository.HallReservationRepository;
import org.shreejalarammandir.repository.PujariSevaRepository;
import org.shreejalarammandir.service.AppPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The only calendar writer. Replaces the BT_PC placeholder that returned a mock
 * String id or null from the public hall controller after submit. That placeholder
 * method is deleted — do not add it back. Never injects CateringService.
 */
@Service
public class GoogleCalendarService {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarService.class);

    private final GoogleCalendarProperties properties;
    private final GoogleCalendarClient client;
    private final HallCalendarDescriptionBuilder hallDescriptionBuilder;
    private final PujariCalendarDescriptionBuilder pujariDescriptionBuilder;
    private final AppPropertyService appPropertyService;
    private final HallReservationRepository hallReservationRepository;
    private final PujariSevaRepository pujariSevaRepository;

    public GoogleCalendarService(
            GoogleCalendarProperties properties,
            GoogleCalendarClient client,
            HallCalendarDescriptionBuilder hallDescriptionBuilder,
            PujariCalendarDescriptionBuilder pujariDescriptionBuilder,
            AppPropertyService appPropertyService,
            HallReservationRepository hallReservationRepository,
            PujariSevaRepository pujariSevaRepository) {
        this.properties = properties;
        this.client = client;
        this.hallDescriptionBuilder = hallDescriptionBuilder;
        this.pujariDescriptionBuilder = pujariDescriptionBuilder;
        this.appPropertyService = appPropertyService;
        this.hallReservationRepository = hallReservationRepository;
        this.pujariSevaRepository = pujariSevaRepository;
    }

    public CalendarSyncOutcome syncHallCreated(HallReservation reservation) {
        return syncHall(reservation, false);
    }

    public CalendarSyncOutcome syncHallUpdated(HallReservation reservation) {
        return syncHall(reservation, false);
    }

    public CalendarSyncOutcome syncHallCancelled(HallReservation reservation) {
        return syncHall(reservation, true);
    }

    public CalendarSyncOutcome syncHallContentRefresh(HallReservation reservation) {
        return syncHall(reservation, false);
    }

    public CalendarSyncOutcome syncStandalonePujariCreated(PujariSeva seva) {
        return syncPujari(seva, false);
    }

    public CalendarSyncOutcome syncStandalonePujariUpdated(PujariSeva seva) {
        return syncPujari(seva, false);
    }

    public CalendarSyncOutcome syncStandalonePujariCancelled(PujariSeva seva) {
        return syncPujari(seva, true);
    }

    private CalendarSyncOutcome syncHall(HallReservation reservation, boolean forceCancel) {
        if (!properties.isEnabled()) {
            return CalendarSyncOutcome.notAttempted();
        }
        try {
            boolean softCancel = forceCancel
                    || (reservation.getStatus() != null && reservation.getStatus().isSoftCancelOnCalendar());
            GoogleCalendarEventRequest request = hallRequest(reservation, softCancel);
            String eventId = upsert(reservation.getGoogleCalendarEventId(), request);
            if (eventId != null && !eventId.equals(reservation.getGoogleCalendarEventId())) {
                reservation.setGoogleCalendarEventId(eventId);
                hallReservationRepository.save(reservation);
            }
            return CalendarSyncOutcome.success(eventId);
        } catch (Exception e) {
            log.error("Hall calendar sync failed for publicId={}", reservation.getPublicId(), e);
            return CalendarSyncOutcome.failed();
        }
    }

    private CalendarSyncOutcome syncPujari(PujariSeva seva, boolean forceCancel) {
        if (!properties.isEnabled()) {
            return CalendarSyncOutcome.notAttempted();
        }
        try {
            boolean softCancel = forceCancel
                    || (seva.getStatus() != null && seva.getStatus().isSoftCancelOnCalendar());
            GoogleCalendarEventRequest request = pujariRequest(seva, softCancel);
            String eventId = upsert(seva.getGoogleCalendarEventId(), request);
            if (eventId != null && !eventId.equals(seva.getGoogleCalendarEventId())) {
                seva.setGoogleCalendarEventId(eventId);
                pujariSevaRepository.save(seva);
            }
            return CalendarSyncOutcome.success(eventId);
        } catch (Exception e) {
            log.error("Pujari calendar sync failed for publicId={}", seva.getPublicId(), e);
            return CalendarSyncOutcome.failed();
        }
    }

    private String upsert(String existingId, GoogleCalendarEventRequest request) {
        if (existingId == null || existingId.isBlank()) {
            return client.insert(request);
        }
        try {
            client.patch(existingId, request);
            return existingId;
        } catch (GoogleCalendarNotFoundException e) {
            log.warn("Healing missing Google event {}", existingId);
            return client.insert(request);
        }
    }

    private GoogleCalendarEventRequest hallRequest(HallReservation reservation, boolean softCancel) {
        GoogleCalendarEventRequest request = new GoogleCalendarEventRequest();
        request.setSummary(hallTitle(reservation));
        request.setDescription(hallDescriptionBuilder.build(reservation));
        request.setLocation(appPropertyService.getMandirAddress());
        request.setTimeZone(appPropertyService.getTimezone());
        request.setStart(toOffset(reservation.getReservationDate(), reservation.getStartTime()));
        request.setEnd(toOffset(reservation.getReservationDate(), reservation.getEndTime()));
        request.setStatus(softCancel ? "cancelled" : "confirmed");
        return request;
    }

    private GoogleCalendarEventRequest pujariRequest(PujariSeva seva, boolean softCancel) {
        GoogleCalendarEventRequest request = new GoogleCalendarEventRequest();
        request.setSummary(seva.getSelectedSevas());
        request.setDescription(pujariDescriptionBuilder.build(seva));
        String location = seva.getVenue() == null || seva.getVenue().isBlank()
                ? appPropertyService.getMandirAddress()
                : seva.getVenue();
        request.setLocation(location);
        request.setTimeZone(appPropertyService.getTimezone());
        request.setStart(toOffset(seva.getSevaDate(), seva.getStartTime()));
        request.setEnd(toOffset(seva.getSevaDate(), seva.getEndTime()));
        request.setStatus(softCancel ? "cancelled" : "confirmed");
        return request;
    }

    private static String hallTitle(HallReservation reservation) {
        String title = reservation.getEventTitle() == null ? "Hall reservation" : reservation.getEventTitle();
        if (reservation.getBasement() != null) {
            return title + " (" + reservation.getBasement().name() + ")";
        }
        return title;
    }

    private OffsetDateTime toOffset(LocalDate date, LocalTime time) {
        if (date == null || time == null) {
            return null;
        }
        ZoneId zone = ZoneId.of(appPropertyService.getTimezone());
        return date.atTime(time).atZone(zone).toOffsetDateTime();
    }
}
