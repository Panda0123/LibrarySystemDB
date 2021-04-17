package com.library.database_system.resulttransformer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookTransformer {

    // ALIASES

    // standard
    public static final String BOOK_ID_ALIAS = "bookId";
    public static final String TITLE_ALIAS = "title";
    public static final String PUBLISHED_DATE_ALIAS = "publishedDate";
    public static final String DATE_ADDED_ALIAS = "dateAdded";
    public static final String LANGUAGE_ALIAS = "language";
    public static final String EDITION_ALIAS = "edition";
    public static final String ISBN_ALIAS = "isbn";
    public static final String SUMMARY_ALIAS = "summary";
    public static final String QUANTITY_ALIAS = "quantity";
    public static final String NUM_AVAILABLE_ALIAS = "numAvailable";
    public static final String IMAGE_NAME_ALIAS = "imageName";

    // publisher
    public static final String PUBLISHER_NAME_ALIAS = "publisherName";
    public static final String PUBLISHER_ADDRESS_ALIAS = "publisherAddress";
    public static final String PUBLISHER_ID_ALIAS = "publisherId";

    // category
    public static final String CATEGORY_ID_ALIAS = "categoryId";
    public static final String CATEGORY_NAME_AlIAS = "categoryName";

    // copyright
    public static final String COPYRIGHT_ID_ALIAS = "copyrightId";
    public static final String COPYRIGHT_NAME_ALIAS = "copyrightName";
    public static final String COPYRIGHT_YEAR_ALIAS = "copyrightYear";

    // shelf
    public static final String SHELF_ID_ALIAS = "shelfId";
    public static final String SHELF_NAME_ALIAS = "shelfName";

    // VALUES

    // standard
    private Long bookId;
    private String title;
    private LocalDate publishedDate;
    private LocalDate dateAdded;
    private String language;
    private int edition;
    private String isbn;
    private String summary;
    private String imageName;
    private int quantity;
    private int numAvailable;

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

    private List<AuthorTransformer> authors = new ArrayList<>();

    public BookTransformer(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        Object temp = null;

        this.bookId = (Long)(tuples[aliasToIndexMap.get(BOOK_ID_ALIAS)]);
        this.title = tuples[aliasToIndexMap.get(TITLE_ALIAS)].toString();
        this.publishedDate = (LocalDate)(tuples[aliasToIndexMap.get(PUBLISHED_DATE_ALIAS)]);
        this.dateAdded = (LocalDate)(tuples[aliasToIndexMap.get(DATE_ADDED_ALIAS)]);
        this.language = tuples[aliasToIndexMap.get(LANGUAGE_ALIAS)].toString();
        this.edition = (Integer)(tuples[aliasToIndexMap.get(EDITION_ALIAS)]);
        temp = tuples[aliasToIndexMap.get(ISBN_ALIAS)];
        this.isbn = temp != null ? temp.toString() : null;
        this.summary = tuples[aliasToIndexMap.get(SUMMARY_ALIAS)].toString();
        this.quantity = (Integer)(tuples[aliasToIndexMap.get(QUANTITY_ALIAS)]);
        this.numAvailable = (Integer)(tuples[aliasToIndexMap.get(NUM_AVAILABLE_ALIAS)]);
        this.imageName = tuples[aliasToIndexMap.get(IMAGE_NAME_ALIAS)].toString();

        // publisher
        temp  = tuples[aliasToIndexMap.get(PUBLISHER_NAME_ALIAS)];
        this.publisherName = temp != null ? temp.toString() : null;
        temp  = tuples[aliasToIndexMap.get(PUBLISHER_ADDRESS_ALIAS)];
        this.publisherAddress = temp != null ? temp.toString() : null;
        this.publisherId = Long.parseLong(tuples[aliasToIndexMap.get(PUBLISHER_ID_ALIAS)].toString());

        // category
        temp  = tuples[aliasToIndexMap.get(CATEGORY_NAME_AlIAS)];
        this.categoryName = temp != null ? temp.toString() : null;
        this.categoryId = Long.parseLong(tuples[aliasToIndexMap.get(CATEGORY_ID_ALIAS)].toString());

        // copyright
        temp  = tuples[aliasToIndexMap.get(COPYRIGHT_NAME_ALIAS)];
        this.copyrightName = temp != null ? temp.toString() : null;
        this.copyrightYear = (int)(tuples[aliasToIndexMap.get(COPYRIGHT_YEAR_ALIAS)]);
        this.copyrightId = Long.parseLong(tuples[aliasToIndexMap.get(COPYRIGHT_ID_ALIAS)].toString());

        // shelf
        this.shelfId = Long.parseLong(tuples[aliasToIndexMap.get(SHELF_ID_ALIAS)].toString());
        this.shelfName = tuples[aliasToIndexMap.get(SHELF_NAME_ALIAS)].toString();
    }

    public static String getBookIdAlias() {
        return BOOK_ID_ALIAS;
    }

    public static String getTitleAlias() {
        return TITLE_ALIAS;
    }

    public static String getPublishedDateAlias() {
        return PUBLISHED_DATE_ALIAS;
    }

    public static String getLanguageAlias() {
        return LANGUAGE_ALIAS;
    }

    public static String getDateAddedAlias() {
        return DATE_ADDED_ALIAS;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public static String getEditionAlias() {
        return EDITION_ALIAS;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }

    public static String getPublisherNameAlias() {
        return PUBLISHER_NAME_ALIAS;
    }

    public static String getPublisherAddressAlias() {
        return PUBLISHER_ADDRESS_ALIAS;
    }

    public static String getPublisherIdAlias() {
        return PUBLISHER_ID_ALIAS;
    }

    public static String getCategoryIdAlias() {
        return CATEGORY_ID_ALIAS;
    }

    public static String getCATEGORY_NAME_AlIAS() {
        return CATEGORY_NAME_AlIAS;
    }

    public static String getCopyrightIdAlias() {
        return COPYRIGHT_ID_ALIAS;
    }

    public static String getCopyrightNameAlias() {
        return COPYRIGHT_NAME_ALIAS;
    }

    public static String getCopyrightYearAlias() {
        return COPYRIGHT_YEAR_ALIAS;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
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

    public List<AuthorTransformer> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorTransformer> authors) {
        this.authors = authors;
    }

    public static String getIsbnAlias() {
        return ISBN_ALIAS;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public static String getShelfIdAlias() {
        return SHELF_ID_ALIAS;
    }

    public static String getShelfNameAlias() {
        return SHELF_NAME_ALIAS;
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

    public static String getSummaryAlias() {
        return SUMMARY_ALIAS;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static String getQuantityAlias() {
        return QUANTITY_ALIAS;
    }

    public static String getNumAvailableAlias() {
        return NUM_AVAILABLE_ALIAS;
    }

    public int getQuantity() {
        return quantity;
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

    public static String getImageNameAlias() {
        return IMAGE_NAME_ALIAS;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookTransformer that = (BookTransformer) o;

        return bookId.equals(that.bookId);
    }

    @Override
    public int hashCode() {
        return bookId.hashCode();
    }
}
