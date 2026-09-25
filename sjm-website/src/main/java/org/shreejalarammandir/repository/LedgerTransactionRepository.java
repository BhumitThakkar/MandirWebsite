package org.shreejalarammandir.repository;

import org.shreejalarammandir.model.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, Long> {

    boolean existsByHallReservationIdAndVoidedFalse(Long hallReservationId);

    boolean existsByPujariSevaIdAndVoidedFalse(Long pujariSevaId);
}
