package org.shreejalarammandir.config;

import org.shreejalarammandir.service.calendar.GoogleCalendarApiClient;
import org.shreejalarammandir.service.calendar.GoogleCalendarClient;
import org.shreejalarammandir.service.calendar.NoOpGoogleCalendarClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCalendarClientConfiguration {

    @Bean
    public GoogleCalendarClient googleCalendarClient(GoogleCalendarProperties properties) {
        if (properties.isEnabled()) {
            return new GoogleCalendarApiClient(properties);
        }
        return new NoOpGoogleCalendarClient();
    }
}
