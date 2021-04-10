package com.library.database_system.projections;

import java.time.LocalDate;

public interface BookDetails {


    // standard
    Long getBookId();
    String gettitle();
    LocalDate getpublishedDate();
    String getlanguage();
    // publisher
    String getpublisherName();
    String getpublisherAddress();
    Long getpublisherId();
    // category
    Long getcategoryId();
    String getcategoryName();
    // copyright
    Long getcopyrightId();
    String getcopyrightName();
    String getcopyrightYear();
    // authors
    //Set<Author> getbookAuthors();

    // Set<IdFNameMNameLName> getbookAuthors();
}
