package org.shreejalarammandir.repository;

import java.util.Optional;

import org.shreejalarammandir.model.PujariSeva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PujariSevaRepository extends JpaRepository<PujariSeva, Long> {

    Optional<PujariSeva> findByPublicId(String publicId);
}
