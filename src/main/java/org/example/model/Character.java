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
    private int attack = 0;

    public boolean isAlive() {
        return hp > 0;
    }

    public void receiveDamage(int damage) {
        hp = Math.max(0, hp - damage);
    }
}
