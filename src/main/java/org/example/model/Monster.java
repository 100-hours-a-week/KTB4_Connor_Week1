package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Monster extends Character {
    private final int stage;
    private final int attackChance;

    public Monster(String name, int stage, int hp, int attack, int attackChance) {
        super(name, hp, attack);
        this.stage = stage;
        this.attackChance = attackChance;
    }

    public boolean shouldAttack(int randomValue) {
        return randomValue < attackChance;
    }
}
