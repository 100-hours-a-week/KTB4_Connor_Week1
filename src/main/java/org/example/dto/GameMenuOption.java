package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.model.vo.Option;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum GameMenuOption implements Option {
    PLAY(1, "게임 시작"),
    EXIT(2, "종료");

    private final int number;
    private final String label;
}
