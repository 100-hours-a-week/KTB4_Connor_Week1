package org.example.engine;

import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.vo.BattleOption;

import java.util.Random;

public class BattleEngine {
    private final Random random;

    public BattleEngine() {
        this(new Random());
    }

    public BattleEngine(Random random) {
        this.random = random;
    }

    public BattleTurnResult resolveTurn(Player player, Monster monster, BattleOption playerAction) {
        if (!player.canPerform(playerAction)) {
            throw new IllegalArgumentException("플레이어가 수행할 수 없는 행동입니다.");
        }

        int monsterDamageTaken = monster.receiveDamage(player.damageFor(playerAction));
        if (!monster.isAlive()) {
            return new BattleTurnResult(playerAction, monsterDamageTaken, 0, false, true, false);
        }

        boolean monsterAttacked = monster.shouldAttack(random.nextInt(100));
        int playerDamageTaken = 0;

        if (monsterAttacked) {
            int monsterDamage = playerAction == BattleOption.DEFEND
                    ? Math.max(1, monster.attack() / 2)
                    : monster.attack();
            playerDamageTaken = player.receiveDamage(monsterDamage);
        }

        return new BattleTurnResult(
                playerAction,
                monsterDamageTaken,
                playerDamageTaken,
                monsterAttacked,
                false,
                !player.isAlive()
        );
    }
}
