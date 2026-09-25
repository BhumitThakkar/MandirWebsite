package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.shreejalarammandir.model.BasementOccupancy;
import org.shreejalarammandir.model.Catering;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.HallReservationServiceLine;
import org.shreejalarammandir.model.ReservationStatus;
import org.shreejalarammandir.repository.CateringRepository;
import org.shreejalarammandir.service.HallReservationPujariTitleFormatter;
import org.shreejalarammandir.service.calendar.HallCalendarDescriptionBuilder;

@ExtendWith(MockitoExtension.class)
class HallCalendarDescriptionBuilderTest {

    @Mock
    private CateringRepository cateringRepository;

    private HallCalendarDescriptionBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new HallCalendarDescriptionBuilder(cateringRepository, new HallReservationPujariTitleFormatter());
    }

    @Test
    void includesAllowListAndOmitsMoneyAndPii() {
        HallReservation hall = sampleHall();
        hall.setContactEmail("devotee@example.com");
        hall.setContactPhone("555-0100");
        hall.setContactName("A Devotee");
        hall.setSjmPujariSeva(true);
        hall.replaceServiceLines(List.of(new HallReservationServiceLine("Ganesh puja")));

        Catering catering = new Catering();
        catering.setMealSummary("Lunch thali");
        when(cateringRepository.findByHallReservationId(anyLong())).thenReturn(Optional.of(catering));

        String description = builder.build(hall);

        assertThat(description).contains("publicId: HALL-1");
        assertThat(description).contains("status: SJM_CATERING_PENDING");
        assertThat(description).contains("basement: HALF");
        assertThat(description).contains("guests: 80");
        assertThat(description).contains("nonprofit: yes");
        assertThat(description).contains("catering: Lunch thali");
        assertThat(description).contains("pujari: Ganesh puja");
        assertThat(description).doesNotContain("devotee@example.com");
        assertThat(description).doesNotContain("555-0100");
        assertThat(description).doesNotContain("A Devotee");
        assertThat(description).doesNotContain("$");
        assertThat(description).doesNotContain("amount");
        assertThat(description).doesNotContain("confirmation");
        assertThat(description).doesNotContain("tax");
    }

    private static HallReservation sampleHall() {
        HallReservation hall = new HallReservation();
        hall.setId(9L);
        hall.setPublicId("HALL-1");
        hall.setEventTitle("Wedding");
        hall.setReservationDate(LocalDate.of(2026, 10, 2));
        hall.setStartTime(LocalTime.of(10, 0));
        hall.setEndTime(LocalTime.of(14, 0));
        hall.setStatus(ReservationStatus.SJM_CATERING_PENDING);
        hall.setBasement(BasementOccupancy.HALF);
        hall.setGuestCount(80);
        hall.setNonprofit(true);
        return hall;
    }
}
