package com.library.database_system.repository;

import com.library.database_system.domain.BookCopy;
import com.library.database_system.domain.Reservation;
import com.library.database_system.projections.ReservationProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT reservation FROM Reservation reservation")
    Collection<ReservationProj> findAllReservation();
    Optional<ReservationProj> findReservationById(Long id);

    @Query(value = "SELECT reservation.bookCopy FROM Reservation reservation WHERE reservation.date = current_date() AND reservation.bookCopy.status = 'Available'")
    Collection<BookCopy> updateBookCopyForReservation();
}