package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "section_name_unique", columnNames = "name")})
public class Section {

    @Id
    @SequenceGenerator(
            name = "section_sequence",
            sequenceName = "section_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "section_sequence"
    )
    public Long id;

    public String name;

    @ManyToOne
    @JoinColumn(name = "grade_level_id")
    public GradeLevel grade_level;

    @OneToMany( mappedBy = "section")
    public Set<User> users = new HashSet<>();

    public Section() {
    }

    public Section(String name) {
        this.name = name;
    }

    public Section(String name, GradeLevel grade_level) {
        this.name = name;
        this.grade_level = grade_level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GradeLevel getGradeLevel() {
        return grade_level;
    }

    public void setGradeLevel(GradeLevel grade_level) {
        this.grade_level = grade_level;
    }

    public Set<User> getUsers() {
        return users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        return id.equals(section.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
