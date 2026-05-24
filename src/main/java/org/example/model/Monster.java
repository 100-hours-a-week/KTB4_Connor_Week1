package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Monster extends GameCharacter {

    public Monster(String name, int hp, int attack) {
        super(name, hp, attack);
    }
}
