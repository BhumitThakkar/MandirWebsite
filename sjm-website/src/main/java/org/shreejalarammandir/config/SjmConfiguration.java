package org.shreejalarammandir.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({MandirProperties.class, GoogleCalendarProperties.class})
public class SjmConfiguration {
}
