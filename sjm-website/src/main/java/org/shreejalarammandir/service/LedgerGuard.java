package org.shreejalarammandir.service;

import org.shreejalarammandir.model.HallReservation;
import org.shreejalarammandir.model.PujariSeva;
import org.shreejalarammandir.repository.LedgerTransactionRepository;
import org.springframework.stereotype.Service;

/** Ledger R4: refuse physical delete when an active (non-voided) transaction exists. */
@Service
public class LedgerGuard {

    private final LedgerTransactionRepository transactionRepository;

    public LedgerGuard(LedgerTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void assertDeletable(HallReservation reservation) {
        if (reservation.getId() != null
                && transactionRepository.existsByHallReservationIdAndVoidedFalse(reservation.getId())) {
            throw new NotDeletableException("R4: hall reservation has active ledger entries");
        }
    }

    public void assertDeletable(PujariSeva seva) {
        if (seva.getId() != null
                && transactionRepository.existsByPujariSevaIdAndVoidedFalse(seva.getId())) {
            throw new NotDeletableException("R4: pujari seva has active ledger entries");
        }
    }
}
