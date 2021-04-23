package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table
@Entity
public class Copyright {

    @Id
    @SequenceGenerator(
            name = "copyright_sequence",
            sequenceName = "copyright_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "copyright_sequence")
    private Long id;

    @OneToMany(mappedBy = "copyright")
    Set<Book> books = new HashSet<>();

    private String name;

    private Short year;

    public Copyright() {
    }

    public Copyright(String name, Short year) {
        this.name = name;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short yr) {
        this.year = yr;
    }



    @Override
    public String toString() {
        return "Copyright{" +
                "id=" + id +
                ", books=" + books +
                ", name='" + name + '\'' +
                ", yr=" + year +
                '}';
    }
}
