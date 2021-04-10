package com.library.database_system.resulttransformer;

import org.hibernate.transform.ResultTransformer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UserDetailsResultTransformer implements ResultTransformer {
     /*
        For Book Details
     */
    private Map<String, UserTransformer> userDTOMap = new LinkedHashMap<>();
    private Map<Long, BookTransformer> bookDTOMap = new LinkedHashMap<>();
    private Map<Long, AuthorTransformer> authorDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {

        Map<String, Integer> aliasToIndexMapDct = Utilities.aliasToIndexMap(aliases);

        // users
        String userId = (tuple[aliasToIndexMapDct.get(UserTransformer.USER_ID_ALIAS)]).toString();
        UserTransformer userTransformer = userDTOMap.computeIfAbsent(
                userId,
                id -> new UserTransformer(tuple, aliasToIndexMapDct)
        );

        // book
        Long bookId = (Long)(tuple[aliasToIndexMapDct.get(BookTransformer.BOOK_ID_ALIAS)]);
        BookTransformer bookTransformer = bookDTOMap.computeIfAbsent(
                bookId,
                id -> new BookTransformer(tuple, aliasToIndexMapDct)
        );
        // author
        Long authorId = (Long)(tuple[aliasToIndexMapDct.get(AuthorTransformer.AUTHOR_ID_ALIAS)]);
        AuthorTransformer authorTransformer = authorDTOMap.computeIfAbsent(
                authorId,
                id -> new AuthorTransformer(tuple, aliasToIndexMapDct)
        );

        // add author to book
        if (!bookTransformer.getAuthors().contains(authorTransformer)) {
            bookTransformer.getAuthors().add(authorTransformer);
        }

        // add book to user
        if (!userTransformer.getBorrowed().contains(bookTransformer)) {
            userTransformer.getBorrowed().add( bookTransformer );
        }

        return userTransformer;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(userDTOMap.values());
    }
}
