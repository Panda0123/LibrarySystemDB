package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

public interface CollectionProj {
    String getId();
    String getTitle();
    @Value("#{target.ISBN}")
    String getIsbn();
    Integer getEdition();
    String getLanguage();
    @Value("#{target.category.name}")
    String getClassification();
}
