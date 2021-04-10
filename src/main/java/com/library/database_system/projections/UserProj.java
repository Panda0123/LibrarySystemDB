package com.library.database_system.projections;

import org.springframework.beans.factory.annotation.Value;

public interface UserProj {
    String getId();
    @Value("#{target.f_name}")
    String getfName();
    @Value("#{target.m_name}")
    String getmName();
    @Value("#{target.l_name}")
    String getlName();
    String getAddress();
    @Value("#{target.userType}")
    String getType();
    @Value("#{target.section}")
    SectionDTO getSectionDTO();

    interface SectionDTO {
        @Value("#{target.name}")
        String getName();
        @Value("#{target.id}")
        String getId();
    }
}
