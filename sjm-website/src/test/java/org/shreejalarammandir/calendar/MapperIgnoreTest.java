package org.shreejalarammandir.calendar;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.shreejalarammandir.dto.HallReservationAdminForm;
import org.shreejalarammandir.dto.HallReservationForm;
import org.shreejalarammandir.dto.PujariSevaAdminForm;
import org.shreejalarammandir.dto.PujariSevaForm;
import org.shreejalarammandir.mapper.HallReservationMapper;
import org.shreejalarammandir.mapper.PujariSevaMapper;
import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.model.PujariSevaStatus;
import org.shreejalarammandir.model.ReservationStatus;

class MapperIgnoreTest {

    private final HallReservationMapper hallMapper = Mappers.getMapper(HallReservationMapper.class);
    private final PujariSevaMapper pujariMapper = Mappers.getMapper(PujariSevaMapper.class);

    @Test
    void hallToEntityIgnoresClientSuppliedCalendarId() {
        HallReservationForm form = new HallReservationForm();
        form.setEventTitle("Navratri");
        form.setReservationDate(LocalDate.of(2026, 10, 10));
        form.setStartTime(LocalTime.of(18, 0));
        form.setEndTime(LocalTime.of(21, 0));
        form.setGoogleCalendarEventId("client-forged-id");

        HallReservation entity = hallMapper.toEntity(form);

        assertThat(entity.getEventTitle()).isEqualTo("Navratri");
        assertThat(entity.getGoogleCalendarEventId()).isNull();
        assertThat(entity.getStatus()).isNull();
    }

    @Test
    void hallUpdateEntityIgnoresClientSuppliedCalendarId() {
        HallReservation existing = new HallReservation();
        existing.setGoogleCalendarEventId("real-google-id");
        existing.setPublicId("hall-KEEP");

        HallReservationAdminForm form = new HallReservationAdminForm();
        form.setEventTitle("Updated");
        form.setReservationDate(LocalDate.of(2026, 11, 1));
        form.setStartTime(LocalTime.of(9, 0));
        form.setEndTime(LocalTime.of(11, 0));
        form.setStatus(ReservationStatus.APPROVED);
        form.setGoogleCalendarEventId("hacked");

        hallMapper.updateEntity(form, existing);

        assertThat(existing.getEventTitle()).isEqualTo("Updated");
        assertThat(existing.getGoogleCalendarEventId()).isEqualTo("real-google-id");
        assertThat(existing.getPublicId()).isEqualTo("hall-KEEP");
    }

    @Test
    void pujariMappersIgnoreCalendarId() {
        PujariSevaForm create = new PujariSevaForm();
        create.setSevaDate(LocalDate.of(2026, 10, 3));
        create.setStartTime(LocalTime.of(8, 0));
        create.setEndTime(LocalTime.of(9, 0));
        create.setSelectedSevas("Abhishek");
        create.setGoogleCalendarEventId("nope");

        PujariSeva created = pujariMapper.toEntity(create);
        assertThat(created.getGoogleCalendarEventId()).isNull();

        created.setGoogleCalendarEventId("real-pujari-id");
        PujariSevaAdminForm update = new PujariSevaAdminForm();
        update.setSevaDate(create.getSevaDate());
        update.setStartTime(create.getStartTime());
        update.setEndTime(create.getEndTime());
        update.setSelectedSevas("Abhishek");
        update.setStatus(PujariSevaStatus.APPROVED);
        update.setGoogleCalendarEventId("forged");
        pujariMapper.updateEntity(update, created);
        assertThat(created.getGoogleCalendarEventId()).isEqualTo("real-pujari-id");
    }
}
