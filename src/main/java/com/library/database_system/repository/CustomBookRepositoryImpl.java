package com.library.database_system.repository;

import com.library.database_system.resulttransformer.BookTransformer;
import com.library.database_system.resulttransformer.BookDetailsResultTransformer;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class CustomBookRepositoryImpl implements CustomBookRepository {
    private final EntityManager em;
    private final String info = "SELECT bk.id AS bookId, bk.title AS title, bk.publishedDate AS publishedDate, bk.dateAdded AS dateAdded, bk.edition AS edition, bk.summary AS summary, " +
            "bk.quantity AS quantity, bk.numAvailable as numAvailable, bk.imageName AS imageName, " +
            "bk.ISBN AS isbn, bk.language AS language, bk.publisher.name AS publisherName, bk.publisher.address AS publisherAddress, " +
            "bk.publisher.id AS publisherId, bk.category.id AS categoryId, bk.category.name AS categoryName, " +
            "bk.copyright.id AS copyrightId, bk.copyright.name AS copyrightName, bk.copyright.year AS copyrightYear, " +
            "bk.shelf.id AS shelfId, bk.shelf.name as shelfName, " +
            "author.id AS authorId, author.f_name AS authorFName, author.m_name AS authorMName, author.l_name AS authorLName ";

    private final String queryGetBooksDetails = info +
            "FROM com.library.database_system.domain.Book bk " +
            "JOIN bk.authors author " +
            "ORDER BY bk.id";

    private final String queryGetBooksById = info +
            "FROM com.library.database_system.domain.Book bk " +
            "JOIN bk.authors author " +
            "WHERE bk.id = ?1 " +
            "ORDER BY bk.id";

    private final String queryGetBooksDetailsPagination =  info +
            "FROM com.library.database_system.domain.Book bk " +
            "JOIN bk.authors author " +
            "WHERE bk.id IN (?1) ";

    public CustomBookRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public List<BookTransformer> getBooksDetails() {
        List<BookTransformer> bookTransformers = (List<BookTransformer>)em.createQuery(queryGetBooksDetails)
                .unwrap(Query.class)
                .setResultTransformer(new BookDetailsResultTransformer())
                .getResultList();

        return bookTransformers;
    }

    @Override
    public List<BookTransformer> getBooksDetailsPagination(
            int pageNum, int pageSize, String sortBy, String searchKey,
            String filterDateAdded, String filterAuthor, Integer filterFirstPublicationYear,
            Integer filterLastPublicationYear, String filterClassification,
            String filterPublisher, String filterIsbn, String filterLanguage) {
        String queryIds = "SELECT bk.id FROM com.library.database_system.domain.Book bk ";
        String queryBks =  queryGetBooksDetailsPagination;
        String filters = getFilters(searchKey, filterDateAdded, filterAuthor,
                filterFirstPublicationYear, filterLastPublicationYear,
                filterClassification, filterPublisher,
                filterIsbn, filterLanguage);
        if (!filters.equals("")) {
            queryIds += "WHERE" + filters.replaceFirst("\\[S\\]", " ").replace("[S]", " AND ") + " ";
            queryBks += filters.replace("[S]", " AND ") + " ";
        }
        queryIds = queryIds + "ORDER BY ";
        queryBks += "ORDER BY ";
        sortBy = sortBy == null ? "" : sortBy;
        switch (sortBy) {
            case "PublishedDate":
                queryIds += "bk.publishedDate DESC, bk.title ASC";
                queryBks += "bk.publishedDate DESC, bk.title ASC";
                break;
            case "Title":
                queryIds += "bk.title, bk.edition";
                queryBks += "bk.title, bk.edition";
                break;
            case "DateAdded":
            default:
                queryIds += "bk.dateAdded DESC, bk.timeAdded DESC, bk.title ASC";
                queryBks += "bk.dateAdded DESC, bk.timeAdded DESC, bk.title ASC";
        }
        List<Long> ids = getBookIds(pageNum, pageSize, queryIds);
        List<BookTransformer> bookTransformers;
        bookTransformers  = (List<BookTransformer>)em.createQuery(                queryBks)
                .unwrap(Query.class)
                .setParameterList(1, ids)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .setResultTransformer(new BookDetailsResultTransformer())
                .getResultList();
        return bookTransformers;
    }

    public List<Long> getBookIds(int pageNum, int pageSize, String query) {
        List<Long> bookIds;
         bookIds = em.createQuery(query, Long.class)
                .setFirstResult(pageNum*pageSize)
                .setMaxResults(pageSize)
                .getResultList();
       return bookIds;
    }

    public BookTransformer getBookDetails(Long bookId) {
        BookTransformer bookTransformer = (BookTransformer)em.createQuery(queryGetBooksById)
                .unwrap(Query.class)
                .setParameter(1, bookId)
                .setResultTransformer(new BookDetailsResultTransformer())
                .getSingleResult();
        return bookTransformer;
    }

    @Override
    public Long getNumberOfBooks(String searchKey, String filterDateAdded, String filterAuthor,
                          Integer filterFirstPublicationYear, Integer filterLastPublicationYear,
                          String filterClassification, String filterPublisher,
                          String filterIsbn, String filterLanguage) {
        String query = "SELECT count(bk) FROM com.library.database_system.domain.Book bk  ";
        String filters = getFilters(searchKey, filterDateAdded, filterAuthor,
                filterFirstPublicationYear, filterLastPublicationYear,
                filterClassification, filterPublisher,
                filterIsbn, filterLanguage);
        if (!filters.equals(""))
            filters = "WHERE" + filters.replaceFirst("\\[S\\]", " ").replace("[S]", " AND ") + " ";
        query += filters;
        Long numOfResult;
        numOfResult = em.createQuery(query, Long.class).getSingleResult();
        return numOfResult;
    }

    private String getFilters(String searchKey, String filterDateAdded, String filterAuthor,
                              Integer filterFirstPublicationYear, Integer filterLastPublicationYear,
                              String filterClassification, String filterPublisher,
                              String filterIsbn, String filterLanguage) {
        String filters = "";
        if (filterAuthor != null)
            filters += String.format("[S]bk IN (SELECT authored FROM com.library.database_system.domain.Author a INNER JOIN a.booksAuthored authored WHERE concat(a.f_name, ' ', a.m_name, ' ', a.l_name) LIKE '%%%s%%')",
                    filterAuthor);
        if (searchKey != null)
            filters += String.format("[S]bk.title LIKE '%%%s%%'", searchKey);
        if (filterDateAdded != null)
            filters += String.format("[S]bk.dateAdded>='%s'", LocalDate.parse(filterDateAdded));
        if (filterFirstPublicationYear != null)
            filters += String.format("[S]bk.publishedDate>='%s'", LocalDate.of(filterFirstPublicationYear, 1, 1).toString());
        if (filterLastPublicationYear != null)
            filters += String.format("[S]bk.publishedDate<='%s'", LocalDate.of(filterLastPublicationYear, 12, 31).toString());
        if (filterClassification != null)
            filters += String.format("[S]bk.category.name Like '%%%s%%'", filterClassification);
        if (filterPublisher != null)
            filters += String.format("[S]bk.publisher.name Like '%%%s%%'", filterPublisher);
        if (filterIsbn != null)
            filters += String.format("[S]bk.ISBN='%s'", filterIsbn);
        if (filterLanguage != null)
            filters += String.format("[S]bk.language Like '%%%s%%'", filterLanguage);
        return filters;
    }
}
