package com.library.database_system.repository;

import com.library.database_system.resulttransformer.BookDetailsResultTransformer;
import com.library.database_system.resulttransformer.BookTransformer;
import com.library.database_system.resulttransformer.UserDetailsResultTransformer;
import com.library.database_system.resulttransformer.UserTransformer;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final EntityManager em;
    private final String info = "SELECT bk.id AS bookId, bk.title AS title, bk.publishedDate AS publishedDate, bk.dateAdded AS dateAdded, bk.edition AS edition, bk.summary AS summary, " +
            "bk.quantity AS quantity, bk.numAvailable as numAvailable, bk.imageName AS imageName, " +
            "bk.ISBN AS isbn, bk.language AS language, bk.publisher.name AS publisherName, bk.publisher.address AS publisherAddress, " +
            "bk.publisher.id AS publisherId, bk.category.id AS categoryId, bk.category.name AS categoryName, " +
            "bk.copyright.id AS copyrightId, bk.copyright.name AS copyrightName, bk.copyright.year AS copyrightYear, " +
            "bk.shelf.id AS shelfId, bk.shelf.name as shelfName, " +
            "author.id AS authorId, author.f_name AS authorFName, author.m_name AS authorMName, author.l_name AS authorLName, " +
            "user.id AS userId, user.f_name AS userFName, user.m_name AS userMName, user.l_name AS userLName, user.address AS userAddress ";

    private final String queryGetUserDetails = info +
            "FROM com.library.database_system.domain.User user " +
            // "JOIN user.borrowed.book as bk " +
            "JOIN user.borrowed as borrowed " +
            "JOIN borrowed.bookCopy as bkCpy " + // if does not work
            "JOIN bkCpy.originalBook as bk " +
            "JOIN bk.authors author " +
            "ORDER BY user.id";

//    private final String queryGetBooksById = info +
//            "FROM com.library.database_system.domain.Book bk " +
//            "JOIN bk.authors author " +
//            "WHERE bk.id = ?1 " +
//            "ORDER BY bk.id";
//
//    private final String queryGetBooksDetailsPagination =  info +
//            "FROM com.library.database_system.domain.Book bk " +
//            "JOIN bk.authors author " +
//            "WHERE bk.id IN (?1) ";

    public CustomUserRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public List<UserTransformer> getUsersDetails() {
        List<UserTransformer> userTransformers = (List<UserTransformer>)em.createQuery(queryGetUserDetails)
                .unwrap(Query.class)
                .setResultTransformer(new UserDetailsResultTransformer())
                .getResultList();

        return userTransformers;
    }
}
