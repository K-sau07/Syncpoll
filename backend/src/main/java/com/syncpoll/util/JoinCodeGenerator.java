package com.syncpoll.util;

import java.security.SecureRandom;

public class JoinCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a random 6-character join code.
     * Excludes easily confused characters (0, O, 1, I).
     */
    public static String generate() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    /**
     * Validates if a given code matches the expected format.
     */
    public static boolean isValid(String code) {
        if (code == null || code.length() != CODE_LENGTH) {
            return false;
        }
        return code.chars().allMatch(c -> CHARACTERS.indexOf(c) >= 0);
    }
}
