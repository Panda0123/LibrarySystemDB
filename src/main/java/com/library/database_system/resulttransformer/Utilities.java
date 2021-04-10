package com.library.database_system.resulttransformer;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utilities {

    public static Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i], i);
        }

        return aliasToIndexMap;
    }
}
