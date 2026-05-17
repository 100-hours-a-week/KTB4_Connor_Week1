package org.example.model.vo;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum JobOption implements Option {
    WARRIOR(1, "Warrior (전사)"),
    MAGE(2, "Mage (메이지)");

    private final int number;
    private final String label;

    JobOption(int number, String label) {
        this.number = number;
        this.label = label;
    }
}
