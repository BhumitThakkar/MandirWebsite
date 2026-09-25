package org.shreejalarammandir.service;

import java.util.stream.Collectors;

import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.HallReservationServiceLine;
import org.springframework.stereotype.Component;

@Component
public class HallReservationPujariTitleFormatter {

    public String format(HallReservation reservation) {
        if (reservation == null || !reservation.isSjmPujariSeva() || reservation.getServiceLines() == null) {
            return "";
        }
        return reservation.getServiceLines().stream()
                .map(HallReservationServiceLine::getTitle)
                .filter(title -> title != null && !title.isBlank())
                .collect(Collectors.joining(", "));
    }
}
