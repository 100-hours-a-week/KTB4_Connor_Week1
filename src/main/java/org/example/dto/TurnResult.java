package org.example.dto;

import lombok.Builder;
import org.example.engine.BattleTurnResult;
import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.vo.BattleOption;

@Builder
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
    public static TurnResult from(final Player player,
                                  final Monster monster,
                                  final BattleTurnResult result) {
        return TurnResult.builder()
                .playerAction(result.playerAction())
                .playerName(player.name())
                .monsterName(monster.name())
                .monsterDamageTaken(result.monsterDamageTaken())
                .playerDamageTaken(result.playerDamageTaken())
                .monsterAttacked(result.monsterAttacked())
                .monsterDefeated(result.monsterDefeated())
                .playerDefeated(result.playerDefeated())
                .playerHp(player.hp())
                .playerMaxHp(player.maxHp())
                .monsterHp(monster.hp())
                .build();
    }
}
