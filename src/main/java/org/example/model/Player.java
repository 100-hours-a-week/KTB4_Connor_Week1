package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.model.vo.BattleOption;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public abstract class Player extends Character {
    private final int maxHp;
    private final Set<BattleOption> availableBattleOptions;

    protected Player(String name, int hp, int attack, Set<BattleOption> availableBattleOptions) {
        super(name, hp, attack);
        this.maxHp = hp;
        this.availableBattleOptions = Set.copyOf(EnumSet.copyOf(availableBattleOptions));
    }

    public boolean canPerform(BattleOption battleOption) {
        return battleOption != null && availableBattleOptions.contains(battleOption);
    }

    public int damageFor(BattleOption battleOption) {
        if (!canPerform(battleOption)) {
            throw new IllegalArgumentException("수행할 수 없는 행동입니다.");
        }

        return switch (battleOption) {
            case ATTACK -> attack();
            case DEFEND -> 0;
            case SKILL -> attack() * 2;
        };
    }
}
