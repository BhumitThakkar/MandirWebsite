package org.shreejalarammandir.service.calendar;

/**
 * Single HTTP/API boundary for Google Calendar. Tests mock this.
 * Never generate placeholder CAL_ ids here.
 */
public interface GoogleCalendarClient {

    String insert(GoogleCalendarEventRequest request);

    void patch(String eventId, GoogleCalendarEventRequest request);

    void cancel(String eventId);
}
