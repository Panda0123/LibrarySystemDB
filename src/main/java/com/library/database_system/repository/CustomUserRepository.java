package com.library.database_system.repository;

import com.library.database_system.resulttransformer.UserTransformer;

import java.util.List;

public interface CustomUserRepository {
    // all
    List<UserTransformer> getUsersDetails();
    // pagination
    //List<BookTransformer> getBooksDetailsPagination(int pageNum, int pageSize, String sortBy, String searchKey);
    // one
    //BookTransformer getBookDetails(Long bookId);

}
