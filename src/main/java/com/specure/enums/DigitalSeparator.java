package com.specure.enums;

public enum DigitalSeparator {
    COMMA("comma", ','),
    DOT("dot", '.');

    private final String value;
    private final char separator;

    DigitalSeparator(String value, char separator) {
        this.value = value;
        this.separator = separator;
    }

    public String getValue() {
        return value;
    }

    public char getSeparator() {
        return separator;
    }
}
