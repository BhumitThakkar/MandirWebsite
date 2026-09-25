package org.shreejalarammandir.repository;

import java.util.Optional;

import org.shreejalarammandir.model.HallReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallReservationRepository extends JpaRepository<HallReservation, Long> {

    Optional<HallReservation> findByPublicId(String publicId);
}
