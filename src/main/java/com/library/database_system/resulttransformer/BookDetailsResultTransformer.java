package com.library.database_system.resulttransformer;

import org.hibernate.transform.ResultTransformer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookDetailsResultTransformer implements ResultTransformer {
    /*
        For Book Details
     */

    private Map<Long, BookTransformer> bookDTOMap = new LinkedHashMap<>();
    private Map<Long, AuthorTransformer> authorDTOMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {

            Map<String, Integer> aliasToIndexMapDct = Utilities.aliasToIndexMap(aliases);

            Long bookId = (Long)(tuple[aliasToIndexMapDct.get(BookTransformer.BOOK_ID_ALIAS)]);

            BookTransformer bookTransformer = bookDTOMap.computeIfAbsent(
                    bookId,
                    id -> new BookTransformer(tuple, aliasToIndexMapDct)
            );

            Long authorId = (Long)(tuple[aliasToIndexMapDct.get(AuthorTransformer.AUTHOR_ID_ALIAS)]);
            AuthorTransformer authorTransformer = authorDTOMap.computeIfAbsent(
                    authorId,
                    id -> new AuthorTransformer(tuple, aliasToIndexMapDct)
            );

            // TODO: add author map so i don't have to create author all the time
            bookTransformer.getAuthors().add( authorTransformer );

            return bookTransformer;
    }

    @Override
    public List transformList(List collection) {
        return new ArrayList<>(bookDTOMap.values());
    }

//    public  Map<String, Integer> aliasToIndexMap(
//            String[] aliases) {
//
//        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();
//
//        for (int i = 0; i < aliases.length; i++) {
//            aliasToIndexMap.put(aliases[i], i);
//        }
//
//        return aliasToIndexMap;
//    }
}