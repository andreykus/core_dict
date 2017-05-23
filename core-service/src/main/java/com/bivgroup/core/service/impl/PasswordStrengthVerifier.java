package com.bivgroup.core.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 15.08.2016.
 */
public class PasswordStrengthVerifier {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    public static final int SET_ASCII_ALPHANUM = 0;
    public static final int SET_RUSSIAN = 1;
    public static final int SET_ADDITIONAL = 2;
    public static final int MAX_REPEAT_COUNT_DEFAULT = 2;
    public static final int MIN_GROUPS_DEFAULT = 3;
    private static final char[] ADDITIONAL = new char[]{'!', '.', ',', '-', '_', '@', '#', '$', '%', '^', '&', ' '};
    private static final char RLMIN = 'а';
    private static final char RLMAX = 'я';
    private static final char RUMIN = 'А';
    private static final char RUMAX = 'Я';
    private final int minlen;
    private final int maxlen;
    private final int maxRepeatCount;
    private final int minGroups;
    private final boolean russianAllowed;
    private final boolean additionalAllowed;

    public PasswordStrengthVerifier(int minlen, int maxlen) {
        this(minlen, maxlen, 2, 3, 0);
    }

    public PasswordStrengthVerifier(int minlen, int maxlen, int maxRepeatCount, int minGroups, int allowedFlags) {
        this.minlen = minlen;
        this.maxlen = maxlen;
        this.maxRepeatCount = maxRepeatCount;
        this.minGroups = minGroups;
        this.russianAllowed = (allowedFlags & 1) != 0;
        this.additionalAllowed = (allowedFlags & 2) != 0;
    }

    public PasswordStrengthVerifier.Result isPasswordValid(String password) {
        if (password == null) {
            return PasswordStrengthVerifier.Result.ERROR_NA;
        } else if (password.length() < this.minlen) {
            return PasswordStrengthVerifier.Result.ERROR_SHORT;
        } else if (password.length() > this.maxlen) {
            return PasswordStrengthVerifier.Result.ERROR_LONG;
        } else {
            char prevChar = 0;
            int prevCount = 0;
            int low = 0;
            int up = 0;
            int digit = 0;
            int additional = 0;
            char[] chars = password.toCharArray();
            char[] groups = chars;
            int len$ = chars.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                char c = groups[i$];
                if (this.isLow(c)) {
                    ++low;
                } else if (this.isUp(c)) {
                    ++up;
                } else if (this.isDigit(c)) {
                    ++digit;
                } else {
                    if (!this.additionalAllowed || !this.isAdditional(c)) {
                        return PasswordStrengthVerifier.Result.ERROR_INVALID_CHAR;
                    }

                    ++additional;
                }

                if (c == prevChar) {
                    ++prevCount;
                } else {
                    prevChar = c;
                    prevCount = 1;
                }

                if (prevCount > this.maxRepeatCount) {
                    return PasswordStrengthVerifier.Result.ERROR_MAX_REPEAT;
                }
            }

            int var13 = this.sign(low) + this.sign(up) + this.sign(digit) + this.sign(additional);
            if (var13 < this.minGroups) {
                return PasswordStrengthVerifier.Result.ERROR_LOW_COMPLEXITY;
            } else {
                return PasswordStrengthVerifier.Result.OK;
            }
        }
    }

    private boolean isLow(char c) {
        return 97 <= c && c <= 122 || this.russianAllowed && 1072 <= c && c <= 1103;
    }

    private boolean isUp(char c) {
        return 65 <= c && c <= 90 || this.russianAllowed && 1040 <= c && c <= 1071;
    }

    private boolean isDigit(char c) {
        return 48 <= c && c <= 57;
    }

    private boolean isAdditional(char c) {
        char[] arr$ = ADDITIONAL;
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            char ac = arr$[i$];
            if (ac == c) {
                return true;
            }
        }

        return false;
    }

    private int sign(int arg) {
        return arg == 0 ? 0 : (arg > 0 ? 1 : -1);
    }

    public static enum Result {
        OK("Пароль правильный"),
        ERROR_NA("Пароль не задан (пустой)"),
        ERROR_SHORT("Длина пароля меньше допустимой минимальной"),
        ERROR_LONG("Длина пароля больше допустимой максимальной"),
        ERROR_INVALID_CHAR("Пароль содержит недопустимые символы"),
        ERROR_MAX_REPEAT("Символ повторяется слишком большое количество раз подряд"),
        ERROR_LOW_COMPLEXITY("Пароль слишком простой (содержит мало групп символов)");

        private String description;

        private Result(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
