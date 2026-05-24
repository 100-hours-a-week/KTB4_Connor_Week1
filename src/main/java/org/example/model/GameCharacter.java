package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class GameCharacter implements Attackable {
    private final String name;
    private int hp;
    private final int attack;

    public GameCharacter(String name, int hp, int attack) {
        validateName(name);
        validateHp(hp);
        validateAttack(attack);
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    public int attack(final AttackStrategy strategy) {
        if (strategy.attackable()) {
            return attack;
        }
        return 0;
    }

    public void damage(int attack) {
        hp = Math.max(0, hp - attack);
    }

    public boolean isAlive() {
        return hp > 0;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
    }

    private void validateHp(int hp) {
        if (hp < 0) {
            throw new IllegalArgumentException("체력은 음수일 수 없습니다.");
        }
    }

    private void validateAttack(int attack) {
        if (attack < 0) {
            throw new IllegalArgumentException("공격력은 음수일 수 없습니다.");
        }
    }
}
