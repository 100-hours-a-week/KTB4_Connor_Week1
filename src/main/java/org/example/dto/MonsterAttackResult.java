package org.example.dto;

import org.example.model.Monster;
import org.example.model.Player;

import java.util.Objects;

public record MonsterAttackResult(
        String monsterName,
        String playerName,
        int playerDamageTaken,
        boolean playerDefeated,
        int playerHp,
        int playerMaxHp
) {
    public static MonsterAttackResult of(final Player player,
                                         final Monster monster,
                                         final int playerDamageTaken) {
        Player safePlayer = Objects.requireNonNull(player);
        Monster safeMonster = Objects.requireNonNull(monster);

        return new MonsterAttackResult(
                safeMonster.name(),
                safePlayer.name(),
                playerDamageTaken,
                !safePlayer.isAlive(),
                safePlayer.hp(),
                safePlayer.maxHp()
        );
    }
}
