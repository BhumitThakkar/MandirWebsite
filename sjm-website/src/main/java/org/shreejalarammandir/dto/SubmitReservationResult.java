package org.shreejalarammandir.dto;

import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.service.calendar.CalendarSyncOutcome;

public class SubmitReservationResult {

    private final HallReservation reservation;
    private final CalendarSyncOutcome calendarOutcome;

    public SubmitReservationResult(HallReservation reservation, CalendarSyncOutcome calendarOutcome) {
        this.reservation = reservation;
        this.calendarOutcome = calendarOutcome;
    }

    public HallReservation getReservation() {
        return reservation;
    }

    public CalendarSyncOutcome getCalendarOutcome() {
        return calendarOutcome;
    }
}
