package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface BorrowProj {
    @Value("#{target.id}")
    Long getBorrowId();

    @Value("#{target.user}")
    UserProj getUserDTO();

    @Value("#{target.borrowedDate}")
    LocalDate getIssueDate();

    @Value("#{target.returnedDate}")
    LocalDate getReturnedDate();

    @Value("#{target.dueDate}")
    LocalDate getDueDate();

    // this
//    @Value("#{target.bookCopy}")
//    BookCopyProj getBookCopyDTO();

    // or all this
    @Value("#{target.bookCopy.id}")
    Long getBkCpyId();

    @Value("#{target.bookCopy.copy_num}")
    Long getBkCpyNum();

    @Value("#{target.bookCopy.originalBook.title}")
    String getTitle();

    @Value("#{target.bookCopy.originalBook.id}")
    Long getOriginalBookId();

}
