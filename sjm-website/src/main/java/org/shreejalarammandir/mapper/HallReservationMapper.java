package org.shreejalarammandir.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.shreejalarammandir.dto.HallReservationAdminForm;
import org.shreejalarammandir.dto.HallReservationForm;
import org.shreejalarammandir.model.HallReservation;

@Mapper(componentModel = "spring")
public interface HallReservationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNotes", ignore = true)
    @Mapping(target = "googleCalendarEventId", ignore = true)
    @Mapping(target = "paymentValidated", ignore = true)
    @Mapping(target = "serviceLines", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    HallReservation toEntity(HallReservationForm form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "googleCalendarEventId", ignore = true)
    @Mapping(target = "paymentValidated", ignore = true)
    @Mapping(target = "contactName", ignore = true)
    @Mapping(target = "contactEmail", ignore = true)
    @Mapping(target = "contactPhone", ignore = true)
    @Mapping(target = "serviceLines", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(HallReservationAdminForm form, @MappingTarget HallReservation entity);
}
