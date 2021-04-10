package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "gradeLevel_level_unique", columnNames = "level")})
public class GradeLevel {

    @Id
    @SequenceGenerator(
            name = "gradeLevel_sequence",
            sequenceName = "gradeLevel_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "gradeLevel_sequence"
    )
    public Long id;
    public int level;

    @OneToMany(mappedBy = "grade_level")
    public Set<Section> sections = new HashSet<>();

    public GradeLevel() {
    }

    public GradeLevel(int level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Set<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GradeLevel that = (GradeLevel) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "GradeLevel{" +
                "id=" + id +
                ", level=" + level +
                '}';
    }
}
