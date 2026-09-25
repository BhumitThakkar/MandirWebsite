package org.shreejalarammandir.repository;

import java.util.Optional;

import org.shreejalarammandir.model.Catering;
import org.shreejalarammandir.model.HallReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CateringRepository extends JpaRepository<Catering, Long> {

    Optional<Catering> findByHallReservation(HallReservation hallReservation);

    Optional<Catering> findByHallReservationId(Long hallReservationId);

    void deleteByHallReservationId(Long hallReservationId);
}
