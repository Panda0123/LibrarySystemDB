package com.library.database_system.domain;

import javax.persistence.*;

@Entity
@Table(name = "book_copy")
public class BookCopy {

    @Id
    @SequenceGenerator(
            name = "book_copy_sequence",
            sequenceName = "book_copy_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_copy_sequence"
    )
    private Long id;

    private int copy_num;

    private String status;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book originalBook;

    @OneToOne( mappedBy = "bookCopy")
    private Borrow borrower;

    @OneToOne( mappedBy = "bookCopy")
    private Reservation reserved;

//    @OneToMany( mappedBy = "bookCopy")
//    private Set<Borrow> reservation = new HashSet<>();

    public BookCopy() {

    }

    public BookCopy(int copy_num, String status) {
        this.copy_num = copy_num;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public int getCopy_num() {
        return copy_num;
    }

    public void setCopy_num(int copy_num) {
        this.copy_num = copy_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Book getBook() {
        return originalBook;
    }


    public Book getOriginalBook() {
        return originalBook;
    }

    public void setOriginalBook(Book originalBook) {
        this.originalBook = originalBook;
    }

    public Borrow getBorrower() {
        return this.borrower;
    }

    public Reservation getReserved() {
        return reserved;
    }

    public void setReserved(Reservation reserved) {
        this.reserved = reserved;
    }

    public void setBorrower(Borrow borrower) {
        this.borrower = borrower;
    }

    public void setBook(Book book) {
        this.originalBook = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookCopy bookCopy = (BookCopy) o;

        return id.equals(bookCopy.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
