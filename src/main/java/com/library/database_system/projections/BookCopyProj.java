package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface BookCopyProj {

    Long getId();
    int getCopy_num();
    String getStatus();

    @Value("#{target.reserved != null ? target.reserved.date : null}")
    LocalDate getReserved_date();
}
