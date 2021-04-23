package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface ReservationProj {
    @Value("#{target.id}")
    Long getReservationId();


    @Value("#{target.user}")
    UserProj getUserDTO();

    @Value("#{target.date}")
    LocalDate getReservedDate();


    @Value("#{target.bookCopy.id}")
    Long getBkCpyId();

    @Value("#{target.bookCopy.copy_num}")
    Long getBkCpyNum();

    @Value("#{target.bookCopy.originalBook.title}")
    String getTitle();

    @Value("#{target.bookCopy.originalBook.id}")
    Long getOriginalBookId();

    @Value("#{target.bookCopy.originalBook.ISBN}")
    String getIsbn();

    @Value("#{target.bookCopy.originalBook.edition}")
    String getEdition();

    @Value("#{target.bookCopy.originalBook.imageName}")
    String getImageName();

}
