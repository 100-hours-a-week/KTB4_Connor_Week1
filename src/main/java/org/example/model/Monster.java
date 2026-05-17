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
        if (stage < 1) {
            throw new IllegalArgumentException("스테이지는 1 이상이어야 합니다.");
        }
        if (attackChance < 0 || attackChance > 100) {
            throw new IllegalArgumentException("공격 확률은 0 이상 100 이하여야 합니다.");
        }
        this.stage = stage;
        this.attackChance = attackChance;
    }

    public boolean shouldAttack(int randomValue) {
        return randomValue < attackChance;
    }
}
