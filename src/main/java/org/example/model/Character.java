package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Accessors(fluent = true)
public class Character {
    private String name;
    private int hp;
    private int attack;

    public Character(String name, int hp, int attack) {
        validateName(name);
        validateNonNegative(hp, "체력");
        validateNonNegative(attack, "공격력");
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    public Character(CharacterBuilder<?, ?> builder) {
        this(builder.name, builder.hp, builder.attack);
    }

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

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + "은 음수일 수 없습니다.");
        }
    }
}
