package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Monster extends Character {
    private final int stage;

    public Monster(String name, int stage, int hp, int attack) {
        super(name, hp, attack);
        this.stage = stage;
    }
}
