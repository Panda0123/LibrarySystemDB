package com.library.database_system.onstartup_scheduling;

import com.library.database_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class ScheduledTasks {
    private final ReservationService reservationService;

    @Autowired
    public ScheduledTasks(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Scheduled(cron = "@midnight", zone = "Asia/Singapore")
    public void run() {
        System.out.println("12 A.M. Checks Start");
        System.out.println("Check Reservations");
        this.reservationService.updateBookCopyForReservation();
        System.out.println("Reservations updated");
        System.out.println("12 A.M. Checks Done");
    }
}
