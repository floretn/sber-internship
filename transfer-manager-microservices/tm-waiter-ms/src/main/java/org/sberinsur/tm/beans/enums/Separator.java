package org.sberinsur.tm.beans.enums;

/**
 * Перечисление возможных сепараторов.
 * @author Ненароков П.Ю.
 */
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
