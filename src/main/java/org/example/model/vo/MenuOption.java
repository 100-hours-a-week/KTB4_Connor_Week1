package org.example.model.vo;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public enum MenuOption implements Option {
    PLAY(1, "게임 시작"),
    DESCRIPTION(2, "직업 설명"),
    EXIT(3, "종료");

    private final int number;
    private final String label;

    MenuOption(int number, String label) {
        this.number = number;
        this.label = label;
    }

}
