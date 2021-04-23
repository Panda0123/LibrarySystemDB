package com.library.database_system.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "book",
        uniqueConstraints = {
                  @UniqueConstraint(name = "book_ISBN_unique", columnNames = "ISBN")
        }
)
public class Book {

    @Id
    @SequenceGenerator(
            name = "book_sequence",
            sequenceName = "book_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sequence"
    )
    private Long id;

    @Column( nullable = false )
    private String title;

    @Column(length = 17)
    private String ISBN;

    private int numAvailable;
    private int quantity;

    @Column(length = 100)
    private String language;
    private String edition;
    private LocalDate dateAdded;
    private LocalTime timeAdded;

    private LocalDate publishedDate;

    @Column(length = 10)
    private String imageName;

    @Column( columnDefinition = "TEXT" )
    private String summary;

    @ManyToMany( mappedBy = "booksAuthored")
    private Set<Author> authors = new HashSet<>();

    @OneToMany( mappedBy = "originalBook", cascade = CascadeType.ALL)
    private Set<BookCopy> bookCopy = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id")
    private PublishingHouse publisher;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "copyright_id")
    private Copyright copyright;

    // CONSTRUCTORS
    public Book() {
    }

    public Book(String title, String ISBN, int numAvailable, int quantity, String language, String edition, LocalDate dateAdded,  LocalDate publishedDate, String summary, String imageName, LocalTime timeAdded) {
        this.title = title;
        this.ISBN = ISBN;
        this.numAvailable = numAvailable;
        this.quantity = quantity;
        this.language = language;
        this.edition = edition;
        this.dateAdded = dateAdded;
        this.timeAdded = timeAdded;
        this.publishedDate = publishedDate;
        this.summary = summary;
        this.imageName = imageName;
    }

    // GETTERS AND SETTERS

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public Copyright getCopyright() {
        return copyright;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public LocalTime getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(LocalTime timeAdded) {
        this.timeAdded = timeAdded;
    }

    public Set<BookCopy> getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(Set<BookCopy> bookCopy) {
        this.bookCopy = bookCopy;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublished(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public PublishingHouse getPublisher() {
        return publisher;
    }

    public void setPublisher(PublishingHouse publisher) {
        this.publisher = publisher;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public void setCopyright(Copyright copyright) {
        this.copyright = copyright;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    // OTHER METHODS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + id +
                ", title='" + title + '\'' +
                ", ISBN=" + ISBN +
                ", numAvailable=" + numAvailable +
                ", quantity=" + quantity +
                ", language='" + language + '\'' +
                ", summary='" + summary + '\'' +
                ", edition=" + edition +
                ", dateAdded=" + dateAdded +
                '}';
    }
}