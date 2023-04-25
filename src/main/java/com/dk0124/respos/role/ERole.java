package com.dk0124.respos.role;

public enum ERole {
    USER, ADMIN;

    public static boolean contains(String test) {
        for (ERole eRole : ERole.values()) {
            if (eRole.name().equalsIgnoreCase(test)) {
                return true;
            }
        }
        return false;
    }

    public static ERole getRole(String test) {
        for (ERole eRole : ERole.values()) {
            if (eRole.name().equalsIgnoreCase(test)) {
                return eRole;
            }
        }
        return null;
    }


}
