package com.library.database_system.resulttransformer;

import java.util.Map;

public class AuthorTransformer {

    public static final String AUTHOR_ID_ALIAS = "authorId";
    private static final String F_NAME_ALIAS = "authorFName";
    private static final String M_NAME_ALIAS = "authorMName";
    private static final String L_NAME_ALIAS = "authorLName";

    private Long id;
    private String f_name;
    private String m_name;
    private String l_name;

    public AuthorTransformer(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.id = Long.parseLong(tuples[aliasToIndexMap.get(AUTHOR_ID_ALIAS)].toString());
        this.f_name = tuples[aliasToIndexMap.get(F_NAME_ALIAS)] != null ? tuples[aliasToIndexMap.get(F_NAME_ALIAS)].toString() : null;
        this.m_name = tuples[aliasToIndexMap.get(M_NAME_ALIAS)] != null ? tuples[aliasToIndexMap.get(M_NAME_ALIAS)].toString() : null;
        this.l_name = tuples[aliasToIndexMap.get(L_NAME_ALIAS)] != null ? tuples[aliasToIndexMap.get(L_NAME_ALIAS)].toString() : null;

    }

    public static String getIdAlias() {
        return AUTHOR_ID_ALIAS;
    }

    public static String getfNameAlias() {
        return F_NAME_ALIAS;
    }

    public static String getmNameAlias() {
        return M_NAME_ALIAS;
    }

    public static String getlNameAlias() {
        return L_NAME_ALIAS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthorTransformer that = (AuthorTransformer) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
