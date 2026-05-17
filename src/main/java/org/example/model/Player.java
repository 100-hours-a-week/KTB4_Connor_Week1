package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Accessors(fluent = true)
public class Player extends Character {
    private final int maxHp;

    public Player(String name, int hp, int attack) {
        super(name, hp, attack);
        this.maxHp = hp;
    }
}
