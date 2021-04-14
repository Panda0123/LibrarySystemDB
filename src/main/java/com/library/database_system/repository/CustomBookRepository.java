package com.library.database_system.repository;

import com.library.database_system.resulttransformer.BookTransformer;

import java.time.LocalDate;
import java.util.List;

public interface CustomBookRepository {
    // all
    List<BookTransformer> getBooksDetails();
    // pagination
    List<BookTransformer> getBooksDetailsPagination(
            int pageNum, int pageSize, String sortBy, String searchKey,
            String filterDateAdded, String filterAuthor, Integer filterFirstPublicationYear,
            Integer filterLastPublicationYear, String filterClassification );
    // one
    BookTransformer getBookDetails(Long bookId);
}
