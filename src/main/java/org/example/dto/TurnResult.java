package org.example.dto;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.vo.BattleOption;

import java.util.Objects;

@Builder(access = AccessLevel.PRIVATE)
public record TurnResult(
        BattleOption playerAction,
        String playerName,
        String monsterName,
        int monsterDamageTaken,
        int playerDamageTaken,
        boolean monsterAttacked,
        boolean monsterDefeated,
        boolean playerDefeated,
        int playerHp,
        int playerMaxHp,
        int monsterHp
) {
    public static TurnResult of(final BattleOption playerAction,
                                final Player player,
                                final Monster monster,
                                final int monsterDamageTaken,
                                final int playerDamageTaken,
                                final boolean monsterAttacked,
                                final boolean monsterDefeated) {
        Player safePlayer = Objects.requireNonNull(player);
        Monster safeMonster = Objects.requireNonNull(monster);

        return TurnResult.builder()
                .playerAction(playerAction)
                .playerName(safePlayer.name())
                .monsterName(safeMonster.name())
                .monsterDamageTaken(monsterDamageTaken)
                .playerDamageTaken(playerDamageTaken)
                .monsterAttacked(monsterAttacked)
                .monsterDefeated(monsterDefeated)
                .playerDefeated(!safePlayer.isAlive())
                .playerHp(safePlayer.hp())
                .playerMaxHp(safePlayer.maxHp())
                .monsterHp(safeMonster.hp())
                .build();
    }
}
