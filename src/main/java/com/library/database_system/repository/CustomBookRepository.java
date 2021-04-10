package com.library.database_system.repository;

import com.library.database_system.resulttransformer.BookTransformer;

import java.util.List;

public interface CustomBookRepository {
    // all
    List<BookTransformer> getBooksDetails();
    // pagination
    List<BookTransformer> getBooksDetailsPagination(int pageNum, int pageSize, String sortBy, String searchKey);
    // one
    BookTransformer getBookDetails(Long bookId);
}
