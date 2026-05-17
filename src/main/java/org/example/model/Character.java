package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.*;

@SuperBuilder
@AllArgsConstructor(access = PUBLIC)
@Getter
@Accessors(fluent = true)
public class Character {
    private String name;
    private int hp;
    private int attack;

    public boolean isAlive() {
        return hp > 0;
    }

    public int receiveDamage(int damage) {
        int safeDamage = Math.max(0, damage);
        int nextHp = Math.max(0, hp - safeDamage);
        int actualDamage = hp - nextHp;
        hp = nextHp;
        return actualDamage;
    }
}
