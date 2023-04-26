package com.dk0124.respos.role;

public enum ERole {
    USER, ADMIN;

    public static boolean contains(String param) {
        for (ERole eRole : ERole.values()) {
            if (eRole.name().equalsIgnoreCase(param)) {
                return true;
            }
        }
        return false;
    }

    public static ERole getRole(String param) {
        for (ERole eRole : ERole.values()) {
            if (eRole.name().equalsIgnoreCase(param)) {
                return eRole;
            }
        }
        return null;
    }


}
