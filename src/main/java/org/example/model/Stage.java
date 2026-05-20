package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Stage {

    private int value;

    public Stage(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("스테이지는 1 이상이어야 합니다.");
        }
        this.value = number;
    }

    public void increment() {
        this.value++;
    }
}
