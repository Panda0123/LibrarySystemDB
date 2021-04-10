package com.library.database_system.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class PublishingHouse {

    @Id
    @SequenceGenerator(
            name = "publishing_house_sequence",
            sequenceName = "publishing_house_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "publishing_house_sequence"
    )
    public Long id;

    public String name;

    public String address;

    @OneToMany(mappedBy = "publisher")
    public Set<Book> books = new HashSet<>();

    public PublishingHouse() {
    }

    public PublishingHouse(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublishingHouse that = (PublishingHouse) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "PublishingHouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", books=" + books +
                '}';
    }
}
