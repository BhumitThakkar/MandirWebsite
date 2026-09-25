package org.shreejalarammandir.service;

import org.shreejalarammandir.config.MandirProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AppPropertyService {

    private final String timezone;
    private final MandirProperties mandirProperties;

    public AppPropertyService(
            @Value("${app.timezone:America/Chicago}") String timezone,
            MandirProperties mandirProperties) {
        this.timezone = timezone;
        this.mandirProperties = mandirProperties;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getMandirAddress() {
        return mandirProperties.formattedAddress();
    }
}
