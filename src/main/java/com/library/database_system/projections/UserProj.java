package com.library.database_system.projections;

import com.library.database_system.dtos.GradeLevelDTO;
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
    @Value("#{target.section.grade_level}")
    GradeLevelDTO getGradeLevelDTO();

    interface SectionDTO {
        @Value("#{target.name}")
        String getName();
        @Value("#{target.id}")
        Long getId();
    }

    interface GradeLevelDTO {
        @Value("#{target.level}")
        Integer getLevel();
        @Value("#{target.id}")
        Long getId();
    }
}
