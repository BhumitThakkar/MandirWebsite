package org.shreejalarammandir.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.shreejalarammandir.dto.PujariSevaAdminForm;
import org.shreejalarammandir.dto.PujariSevaForm;
import org.shreejalarammandir.model.PujariSeva;

@Mapper(componentModel = "spring")
public interface PujariSevaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "adminNotes", ignore = true)
    @Mapping(target = "googleCalendarEventId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PujariSeva toEntity(PujariSevaForm form);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "googleCalendarEventId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(PujariSevaAdminForm form, @MappingTarget PujariSeva entity);
}
