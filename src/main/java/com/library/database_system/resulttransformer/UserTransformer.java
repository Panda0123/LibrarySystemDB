package com.library.database_system.resulttransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserTransformer {
    // ALIASES
    public static final String USER_ID_ALIAS = "userId";
    public static final String F_NAME_ALIAS = "userFName";
    public static final String M_NAME_ALIAS = "userMName";
    public static final String L_NAME_ALIAS = "userLName";
    public static final String ADDRESS_ALIAS = "userAddress";

    private String id;
    private String f_name;
    private String m_name;
    private String l_name;
    private String address;

    private List<BookTransformer> borrowed = new ArrayList<>();

    public UserTransformer(
            Object[] tuples,
            Map<String, Integer> aliasToIndexMap) {
        this.id = tuples[aliasToIndexMap.get(USER_ID_ALIAS)].toString();
        this.f_name = tuples[aliasToIndexMap.get(F_NAME_ALIAS)] != null ? tuples[aliasToIndexMap.get(F_NAME_ALIAS)].toString() : null;
        this.m_name = tuples[aliasToIndexMap.get(M_NAME_ALIAS)] != null ? tuples[aliasToIndexMap.get(M_NAME_ALIAS)].toString() : null;
        this.l_name = tuples[aliasToIndexMap.get(L_NAME_ALIAS)] != null ? tuples[aliasToIndexMap.get(L_NAME_ALIAS)].toString() : null;
        this.address = tuples[aliasToIndexMap.get(ADDRESS_ALIAS)] != null ? tuples[aliasToIndexMap.get(L_NAME_ALIAS)].toString() : null;
    }

    public static String getUserIdAlias() {
        return USER_ID_ALIAS;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public static String getAddressAlias() {
        return ADDRESS_ALIAS;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<BookTransformer> getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(List<BookTransformer> borrowed) {
        this.borrowed = borrowed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserTransformer that = (UserTransformer) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}