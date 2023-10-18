package org.sberinsur.tm.dto.entity.test;

public enum Separator {

    TAB('\t'),
    COMMA(';');

    private final char separator;

    Separator(char s) {
        this.separator = s;
    }

    public char getSeparator() {
        return this.separator;
    }
}

