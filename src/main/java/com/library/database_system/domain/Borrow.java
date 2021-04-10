package com.library.database_system.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
public class Borrow {

    @Id
    @SequenceGenerator(
            name = "sequence_borrow",
            sequenceName = "sequence_borrow",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sequence_borrow"
    )
    private Long id;

    @OneToOne()
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate borrowedDate;
    private LocalDate returnedDate;
    private LocalDate dueDate;

    public Borrow() {
    }

    public Borrow(BookCopy bookCopy, User user, LocalDate borrowedDate, LocalDate dueDate) {
        this.bookCopy = bookCopy;
        this.user = user;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Borrow borrow = (Borrow) o;

        return id.equals(borrow.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", book=" + bookCopy +
                ", user=" + user +
                ", borrowedDate=" + borrowedDate +
                ", dueDate=" + dueDate +
                ", returnedDate=" + returnedDate +
                '}';
    }
}
