package com.dk0124.respos.article.domain;

import com.dk0124.respos.role.ERole;

public enum ECategory {
    HUMOR, TECH, FOOD, ETC;

    public static boolean contains(String param) {
        for (ECategory eCategory : ECategory.values()) {
            if (eCategory.name().equalsIgnoreCase(param))
                return true;
        }
        return false;
    }

    public static ECategory getCategory(String param) {
        for (ECategory eCategory : ECategory.values()) {
            if (eCategory.name().equalsIgnoreCase(param))
                return eCategory;
        }
        return null;
    }
}
