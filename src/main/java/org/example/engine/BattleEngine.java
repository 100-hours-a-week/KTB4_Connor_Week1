package org.example.engine;

import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.vo.BattleOption;

public class BattleEngine {
    public BattleTurnResult resolveTurn(Player player, Monster monster, BattleOption playerAction) {
        if (!player.canPerform(playerAction)) {
            throw new IllegalArgumentException("플레이어가 수행할 수 없는 행동입니다.");
        }

        int monsterDamageTaken = monster.receiveDamage(player.damageFor(playerAction));
        if (!monster.isAlive()) {
            return new BattleTurnResult(playerAction, monsterDamageTaken, 0, true, false);
        }

        int monsterDamage = playerAction == BattleOption.DEFEND
                ? Math.max(1, monster.attack() / 2)
                : monster.attack();
        int playerDamageTaken = player.receiveDamage(monsterDamage);

        return new BattleTurnResult(
                playerAction,
                monsterDamageTaken,
                playerDamageTaken,
                false,
                !player.isAlive()
        );
    }
}
