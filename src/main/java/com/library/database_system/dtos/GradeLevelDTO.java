package com.library.database_system.dtos;

public class GradeLevelDTO {
    private Long id;
    private Integer level;

    public GradeLevelDTO(Long id, Integer level) {
        this.id = id;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
