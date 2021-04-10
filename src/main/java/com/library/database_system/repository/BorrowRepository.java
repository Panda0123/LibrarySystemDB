package com.library.database_system.repository;

import com.library.database_system.domain.Borrow;
import com.library.database_system.projections.BorrowProj;
import com.library.database_system.projections.ReservationProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    @Query(value = "SELECT borrow FROM Borrow borrow")
    Collection<BorrowProj> findAllBorrow();
    Optional<BorrowProj> findBorrowById(Long id);
}
