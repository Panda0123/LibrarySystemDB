package com.library.database_system.repository;

import com.library.database_system.projections.CollectionProj;
import com.library.database_system.resulttransformer.BookTransformer;
import com.library.database_system.projections.IdFNameMNameLName;
import com.library.database_system.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository{

    @Query(value = "SELECT * FROM book WHERE book.title = ?1 AND book.edition ?2", nativeQuery = true)
    Optional<Book> findByTitleEdition(String title, int edition);

    @Query(value = "SELECT bk FROM Book bk WHERE bk.ISBN IS NOT NULL AND bk.ISBN = ?1")
    Optional<Book> findByISBN(String ISBN);


    // works
    @Query(value = "SELECT bk.id AS bookId, bk.title AS title, author.id AS id, author.f_name AS f_name, author.m_name AS m_name, author.l_name AS l_name "+
            "FROM com.library.database_system.domain.Book bk JOIN bk.authors author")
    Collection<IdFNameMNameLName> getBookDetails();

    List<BookTransformer> getBooksDetails();

//    @Query(value = "SELECT count(bk.id) FROM com.library.database_system.domain.Book bk")
//    Long getNumberOfBooks();

//    @Query(value = "SELECT count(bk.id) FROM Book bk WHERE bk.title LIKE :searchKey%")
//    Long getNumberOfBooks(@Param("searchKey") String searchKey);

    @Query(value = "SELECT book FROM Book book")
    Collection<CollectionProj> getBooksCollection();
}
