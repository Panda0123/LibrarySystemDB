package com.library.database_system.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Reservation {
    @Id
    @SequenceGenerator(
            name = "reservation_sequence",
            sequenceName = "reservation_sequence"
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_sequence")
    private Long id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    public Reservation() {
    }

    public Reservation(BookCopy bookCopy, User user, LocalDate date) {
        this.bookCopy = bookCopy;
        this.user = user;
        this.date = date;
    }


    public LocalDate getDate() {
        return this.date;
    }



    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookCopy geBookCopy() {
        return this.bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
