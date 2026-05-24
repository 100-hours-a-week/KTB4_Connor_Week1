package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.model.vo.BattleOption;

import java.util.EnumSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
public abstract class Player extends GameCharacter {
    private final int maxHp;
    private final Set<BattleOption> availableBattleOptions;

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

    public final int damageFor(BattleOption battleOption) {
        if (!canPerform(battleOption)) {
            throw new IllegalArgumentException("수행할 수 없는 행동입니다.");
        }

        return damageForAvailableAction(battleOption);
    }

    protected abstract int damageForAvailableAction(BattleOption battleOption);
}
