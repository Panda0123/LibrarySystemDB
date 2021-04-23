package com.library.database_system.onstartup_scheduling;

import com.library.database_system.domain.Admin;
import com.library.database_system.service.AdminService;
import com.library.database_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OnStart implements CommandLineRunner {
    private final ReservationService reservationService;
    private final AdminService adminService;

    @Autowired
    public OnStart(ReservationService reservationService, AdminService adminService) {
        this.reservationService = reservationService;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception  {
        // admin
        if (adminService.isEmpty()) {
            Admin admin = new Admin("admin", "admin");
            if (!this.adminService.contain(admin)) {
                this.adminService.addNewAdmin(admin);
            }
        }
        // check all reservation
        this.reservationService.updateBookCopyForReservation();
        System.out.println("Done..................................");
    }
}
