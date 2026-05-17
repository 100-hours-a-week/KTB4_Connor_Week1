package org.example.model.vo;

public interface Option {
    int number();

    String label();

    default String displayText() {
        return number() + ". " + label();
    }
}
