package org.shreejalarammandir.service.calendar;

import org.shreejalarammandir.model.Catering;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.repository.CateringRepository;
import org.shreejalarammandir.service.HallReservationPujariTitleFormatter;
import org.springframework.stereotype.Component;

/**
 * Builds hall event descriptions. Uses CateringRepository — never CateringService (T8 cycle).
 */
@Component
public class HallCalendarDescriptionBuilder {

    private final CateringRepository cateringRepository;
    private final HallReservationPujariTitleFormatter pujariTitleFormatter;

    public HallCalendarDescriptionBuilder(
            CateringRepository cateringRepository,
            HallReservationPujariTitleFormatter pujariTitleFormatter) {
        this.cateringRepository = cateringRepository;
        this.pujariTitleFormatter = pujariTitleFormatter;
    }

    public String build(HallReservation reservation) {
        return build(reservation, cateringRepository.findByHallReservationId(
                reservation.getId() == null ? -1L : reservation.getId()).orElse(null));
    }

    public String build(HallReservation reservation, Catering catering) {
        StringBuilder sb = new StringBuilder();
        append(sb, "publicId", reservation.getPublicId());
        if (reservation.getStatus() != null) {
            append(sb, "status", reservation.getStatus().name());
        }
        if (reservation.getReservationDate() != null) {
            append(sb, "date", reservation.getReservationDate().toString());
        }
        if (reservation.getStartTime() != null && reservation.getEndTime() != null) {
            append(sb, "time", reservation.getStartTime() + "–" + reservation.getEndTime());
        }
        if (reservation.getBasement() != null) {
            append(sb, "basement", reservation.getBasement().name());
        }
        if (reservation.getGuestCount() != null) {
            append(sb, "guests", String.valueOf(reservation.getGuestCount()));
        }
        append(sb, "nonprofit", reservation.isNonprofit() ? "yes" : "no");
        if (catering != null && catering.getMealSummary() != null && !catering.getMealSummary().isBlank()) {
            append(sb, "catering", catering.getMealSummary());
        }
        String pujariTitles = pujariTitleFormatter.format(reservation);
        if (!pujariTitles.isBlank()) {
            append(sb, "pujari", pujariTitles);
        }
        if (reservation.getPublicId() != null) {
            append(sb, "admin", "/admin/hall/" + reservation.getPublicId());
        }
        return sb.toString().trim();
    }

    private static void append(StringBuilder sb, String label, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append('\n');
        }
        sb.append(label).append(": ").append(value);
    }
}
