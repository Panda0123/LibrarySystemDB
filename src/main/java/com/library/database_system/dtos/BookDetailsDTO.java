package com.library.database_system.dtos;

import com.library.database_system.domain.BookCopy;
import com.library.database_system.projections.IdFNameMNameLName;

import java.time.LocalDate;
import java.util.Set;

public class BookDetailsDTO {

    // standard
    private Long bookId;
    private String title;
    private LocalDate publishedDate;
    private String language;
    private String edition;
    private String isbn;
    private String summary;
    private int quantity;
    private int numAvailable;
    private String imageName;

    // publisher
    private String publisherName;
    private String publisherAddress;
    private Long publisherId;

    // category
    private Long categoryId;
    private String categoryName;

    // copyright
    private Long copyrightId;
    private String copyrightName;
    private int copyrightYear;

    // shelf
    private Long shelfId;
    private String shelfName;

    // authors
    private Set<AuthorDTO> authors;

    // copies
    private Set<BookCopyDTO> copies;

    public BookDetailsDTO() {
    }

    public BookDetailsDTO(Long bookId, String title,
                          LocalDate publishedDate, String language,
                          String edition, String isbn,
                          String summary, int quantity,
                          int numAvailable, String publisherName,
                          String publisherAddress, Long publisherId,
                          Long categoryId, String categoryName,
                          Long copyrightId, String copyrightName,
                          int copyrightYear, Long shelfId,
                          String shelfName, Set<AuthorDTO> authors,
                          String imageName, Set<BookCopyDTO> copies) {
        this.bookId = bookId;
        this.title = title;
        this.publishedDate = publishedDate;
        this.language = language;
        this.edition = edition;
        this.isbn = isbn;
        this.summary = summary;
        this.quantity = quantity;
        this.numAvailable = numAvailable;
        this.publisherName = publisherName;
        this.publisherAddress = publisherAddress;
        this.publisherId = publisherId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.copyrightId = copyrightId;
        this.copyrightName = copyrightName;
        this.copyrightYear = copyrightYear;
        this.shelfId = shelfId;
        this.shelfName = shelfName;
        this.authors = authors;
        this.imageName = imageName;
        this.copies = copies;
    }

    public BookDetailsDTO(String title, LocalDate publishedDate,
                          String language, String edition,
                          String isbn, String summary,
                          int quantity, int numAvailable,
                          String publisherName, String publisherAddress,
                          Long categoryId, String copyrightName,
                          int copyrightYear, String shelfName,
                          String imageName) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.language = language;
        this.edition = edition;
        this.isbn = isbn;
        this.summary = summary;
        this.quantity = quantity;
        this.numAvailable = numAvailable;
        this.publisherName = publisherName;
        this.publisherAddress = publisherAddress;
        this.categoryId = categoryId;
        this.copyrightYear = copyrightYear;
        this.shelfName = shelfName;
        this.imageName = imageName;
    }

    public BookDetailsDTO(String title, LocalDate publishedDate,
                          String language, String edition,
                          String isbn, String summary,
                          int quantity, int numAvailable,
                          String publisherName, String publisherAddress,
                          Long categoryId, String copyrightName,
                          int copyrightYear, String shelfName,
                          Set<AuthorDTO> authors, String imageName) {
        this.title = title;
        this.publishedDate = publishedDate;
        this.language = language;
        this.edition = edition;
        this.isbn = isbn;
        this.summary = summary;
        this.quantity = quantity;
        this.numAvailable = numAvailable;
        this.publisherName = publisherName;
        this.publisherAddress = publisherAddress;
        this.categoryId = categoryId;
        this.copyrightYear = copyrightYear;
        this.shelfName = shelfName;
        this.authors = authors;
        this.imageName = imageName;
    }

    public Long getBookId() {
        return bookId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherAddress() {
        return publisherAddress;
    }

    public void setPublisherAddress(String publisherAddress) {
        this.publisherAddress = publisherAddress;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCopyrightId() {
        return copyrightId;
    }

    public void setCopyrightId(Long copyrightId) {
        this.copyrightId = copyrightId;
    }

    public String getCopyrightName() {
        return copyrightName;
    }

    public void setCopyrightName(String copyrightName) {
        this.copyrightName = copyrightName;
    }

    public int getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(int copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getQuantity() {
        return quantity;
    }

    public Set<BookCopyDTO> getCopies() {
        return copies;
    }

    public void setCopies(Set<BookCopyDTO> copies) {
        this.copies = copies;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Set<AuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorDTO> authors) {
        this.authors = authors;
    }
}
