package org.example.engine;

import org.example.model.vo.BattleOption;

public record BattleTurnResult(
        BattleOption playerAction,
        int monsterDamageTaken,
        int playerDamageTaken,
        boolean monsterAttacked,
        boolean monsterDefeated,
        boolean playerDefeated
) {
}
