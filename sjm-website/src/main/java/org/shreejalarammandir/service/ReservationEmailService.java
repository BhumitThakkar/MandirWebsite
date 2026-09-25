package org.shreejalarammandir.service;

import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.PujariSeva;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Email lives inside submit methods. Calendar is beside this — not inside it.
 * v1 logs only; SMTP is out of calendar-plan scope.
 */
@Service
public class ReservationEmailService {

    private static final Logger log = LoggerFactory.getLogger(ReservationEmailService.class);

    public void sendReservationSummary(HallReservation reservation) {
        log.info("Reservation summary email for hall publicId={}", reservation.getPublicId());
    }

    public void sendPujariSummary(PujariSeva seva) {
        log.info("Reservation summary email for pujari publicId={}", seva.getPublicId());
    }
}
