package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Accessors(fluent = true)
@SuperBuilder
public class Monster extends Character {

    public Monster(String name, int hp, int attack) {
        super(name, hp, attack);
    }
}
