package org.shreejalarammandir.service.calendar;

public class GoogleCalendarNotFoundException extends RuntimeException {

    public GoogleCalendarNotFoundException(String eventId) {
        super("Google Calendar event not found: " + eventId);
    }
}
