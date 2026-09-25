package org.shreejalarammandir.service.calendar;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Present when the feature flag is off so the one writer still has a client bean.
 * GoogleCalendarService must not call it when the flag is off.
 */
@Component
@ConditionalOnMissingBean(GoogleCalendarClient.class)
public class NoOpGoogleCalendarClient implements GoogleCalendarClient {

    @Override
    public String insert(GoogleCalendarEventRequest request) {
        throw new IllegalStateException("Google Calendar client must not be called when the feature flag is off");
    }

    @Override
    public void patch(String eventId, GoogleCalendarEventRequest request) {
        throw new IllegalStateException("Google Calendar client must not be called when the feature flag is off");
    }

    @Override
    public void cancel(String eventId) {
        throw new IllegalStateException("Google Calendar client must not be called when the feature flag is off");
    }
}
