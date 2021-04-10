package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

public interface BookCopyProj {

    Long getId();
    int getCopy_num();
    String getStatus();
//    @Value("#{target.originalBook.title}")
//    String getTitle();
}
