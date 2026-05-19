package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.model.vo.BattleOption;
import org.example.model.vo.JobOption;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public abstract class Player extends Character {
    private final int maxHp;
    private final Set<BattleOption> availableBattleOptions;

    public static Player from(final JobOption jobOption) {
        return switch (jobOption) {
            case WARRIOR -> new Warrior();
            case MAGE -> new Mage();
        };
    }

    protected Player(String name, int hp, int attack, Set<BattleOption> availableBattleOptions) {
        super(name, hp, attack);
        if (availableBattleOptions == null || availableBattleOptions.isEmpty()) {
            throw new IllegalArgumentException("플레이어는 하나 이상의 행동을 가져야 합니다.");
        }
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
