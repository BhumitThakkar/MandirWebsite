package org.shreejalarammandir.service.calendar;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;

import org.shreejalarammandir.config.GoogleCalendarProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

/**
 * Official Google Calendar client. Service account writer, sendUpdates=none, timeouts set.
 * Bean exists only when the feature flag is on; booking flows still work with a mock in tests.
 */
@Component
@ConditionalOnProperty(name = "mandir.google-calendar.enabled", havingValue = "true")
public class GoogleCalendarApiClient implements GoogleCalendarClient {

    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarApiClient.class);
    private static final int CONNECT_TIMEOUT_MS = (int) Duration.ofSeconds(10).toMillis();
    private static final int READ_TIMEOUT_MS = (int) Duration.ofSeconds(20).toMillis();

    private final GoogleCalendarProperties properties;
    private final Calendar calendar;

    public GoogleCalendarApiClient(GoogleCalendarProperties properties) {
        this.properties = properties;
        this.calendar = buildCalendar(properties);
    }

    GoogleCalendarApiClient(GoogleCalendarProperties properties, Calendar calendar) {
        this.properties = properties;
        this.calendar = calendar;
    }

    @Override
    public String insert(GoogleCalendarEventRequest request) {
        try {
            Event created = calendar.events()
                    .insert(requireCalendarId(), toEvent(request))
                    .setSendUpdates("none")
                    .execute();
            return created.getId();
        } catch (IOException e) {
            throw new IllegalStateException("Google Calendar insert failed", e);
        }
    }

    @Override
    public void patch(String eventId, GoogleCalendarEventRequest request) {
        try {
            calendar.events()
                    .patch(requireCalendarId(), eventId, toEvent(request))
                    .setSendUpdates("none")
                    .execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new GoogleCalendarNotFoundException(eventId);
            }
            throw new IllegalStateException("Google Calendar patch failed", e);
        } catch (IOException e) {
            throw new IllegalStateException("Google Calendar patch failed", e);
        }
    }

    @Override
    public void cancel(String eventId) {
        GoogleCalendarEventRequest request = new GoogleCalendarEventRequest();
        request.setStatus("cancelled");
        patch(eventId, request);
    }

    private Event toEvent(GoogleCalendarEventRequest request) {
        Event event = new Event();
        if (request.getSummary() != null) {
            event.setSummary(request.getSummary());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
        }
        if (request.getStatus() != null) {
            event.setStatus(request.getStatus());
        }
        if (request.getStart() != null) {
            event.setStart(toEventDateTime(request.getStart(), request.getTimeZone()));
        }
        if (request.getEnd() != null) {
            event.setEnd(toEventDateTime(request.getEnd(), request.getTimeZone()));
        }
        event.setAttendees(null);
        return event;
    }

    private static EventDateTime toEventDateTime(java.time.OffsetDateTime dateTime, String timeZone) {
        EventDateTime edt = new EventDateTime();
        edt.setDateTime(new DateTime(dateTime.toInstant().toEpochMilli()));
        edt.setTimeZone(timeZone);
        return edt;
    }

    private String requireCalendarId() {
        if (properties.getCalendarId() == null || properties.getCalendarId().isBlank()) {
            throw new IllegalStateException("GOOGLE_CALENDAR_MANDIR_ID / mandir.google-calendar.calendar-id is required");
        }
        return properties.getCalendarId();
    }

    private static Calendar buildCalendar(GoogleCalendarProperties properties) {
        try {
            GoogleCredentials credentials = loadCredentials()
                    .createScoped(Collections.singleton(CalendarScopes.CALENDAR));
            HttpRequestInitializer initializer = request -> {
                new HttpCredentialsAdapter(credentials).initialize(request);
                request.setConnectTimeout(CONNECT_TIMEOUT_MS);
                request.setReadTimeout(READ_TIMEOUT_MS);
            };
            return new Calendar.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), initializer)
                    .setApplicationName("sjm-website")
                    .build();
        } catch (IOException e) {
            log.error("Failed to initialize Google Calendar client for {}", properties.getAccountEmail(), e);
            throw new IllegalStateException("Google Calendar client could not start", e);
        }
    }

    private static GoogleCredentials loadCredentials() throws IOException {
        String json = System.getenv("GOOGLE_CALENDAR_CREDENTIALS_JSON");
        if (json != null && !json.isBlank()) {
            try (InputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
                return GoogleCredentials.fromStream(in);
            }
        }
        String path = System.getenv("GOOGLE_CALENDAR_CREDENTIALS_PATH");
        if (path != null && !path.isBlank()) {
            try (InputStream in = new FileInputStream(path)) {
                return GoogleCredentials.fromStream(in);
            }
        }
        throw new IOException("Set GOOGLE_CALENDAR_CREDENTIALS_JSON or GOOGLE_CALENDAR_CREDENTIALS_PATH");
    }
}
