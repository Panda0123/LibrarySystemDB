package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

public interface CollectionProj {
    String getId();
    String getTitle();
    @Value("#{target.ISBN}")
    String getIsbn();
    String getLanguage();
    @Value("#{target.category.name}")
    String getClassification();
}
