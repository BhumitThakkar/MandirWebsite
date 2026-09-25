package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.shreejalarammandir.service.CateringService;
import org.shreejalarammandir.service.calendar.GoogleCalendarService;
import org.shreejalarammandir.service.calendar.HallCalendarDescriptionBuilder;
import org.shreejalarammandir.support.SjmSpringTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class ContextAndCycleTest extends SjmSpringTest {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private GoogleCalendarService googleCalendarService;
    @Autowired
    private HallCalendarDescriptionBuilder hallCalendarDescriptionBuilder;

    @Test
    void applicationBootsAndCalendarPathDoesNotInjectCateringService() {
        assertThat(applicationContext.getBean(GoogleCalendarService.class)).isNotNull();
        assertThat(applicationContext.getBean(CateringService.class)).isNotNull();

        assertThat(fieldTypes(GoogleCalendarService.class)).doesNotContain(CateringService.class);
        assertThat(fieldTypes(HallCalendarDescriptionBuilder.class)).doesNotContain(CateringService.class);
        assertThat(googleCalendarService).isSameAs(applicationContext.getBean(GoogleCalendarService.class));
        assertThat(hallCalendarDescriptionBuilder).isNotNull();
        assertThat(applicationContext.getBeansOfType(GoogleCalendarService.class)).hasSize(1);
    }

    private static Class<?>[] fieldTypes(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields()).map(Field::getType).toArray(Class<?>[]::new);
    }
}
