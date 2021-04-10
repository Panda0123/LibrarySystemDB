package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Shelf {

    @Id
    @SequenceGenerator(
            name = "shelf_sequence",
            sequenceName = "shelf_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shelf_sequence")
    public Long id;

    @OneToMany( mappedBy = "shelf")
    Set<Book> books = new HashSet<>();

    public String name;

    public Shelf() {
    }

    public Shelf(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        return id == shelf.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "id=" + id +
                ", books=" + books +
                ", name='" + name + '\'' +
                '}';
    }
}
