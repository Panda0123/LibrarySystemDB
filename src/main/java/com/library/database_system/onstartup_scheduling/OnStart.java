package com.library.database_system.onstartup_scheduling;

import com.library.database_system.repository.BookCopyRepository;
import com.library.database_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OnStart implements CommandLineRunner {
    private final ReservationService reservationService;

    @Autowired
    public OnStart(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Override
    public void run(String... args) throws Exception  {
        // check all reservation
        this.reservationService.updateBookCopyForReservation();
        System.out.println("Done..................................");
    }
}
