package com.library.database_system.repository;

import com.library.database_system.domain.Reservation;
import com.library.database_system.dtos.ReservationDTO;
import com.library.database_system.projections.ReservationProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query(value = "SELECT reservation FROM Reservation reservation")
    Collection<ReservationProj> findAllReservation();
    Optional<ReservationProj> findReservationById(Long id);
}