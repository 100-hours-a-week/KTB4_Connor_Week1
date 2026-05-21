package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.dto.TurnResult;
import org.example.model.vo.BattleOption;

import java.util.Objects;

@Getter
@Accessors(fluent = true)
public class Game {
    private final Player player;
    private final MonsterFactory stageManager;
    private final Stage stage;
    private final AttackStrategy attackStrategy;
    private Monster monster;

    private Game(final Player player,
                 final MonsterFactory stageManager) {
        this.player = Objects.requireNonNull(player);
        this.stageManager = Objects.requireNonNull(stageManager);
        this.stage = new Stage(1);
        this.monster = stageManager.create(stage);
        this.attackStrategy = new RandomAttackStrategy();
    }

    public static Game start(final Player player,
                             final MonsterFactory stageManager) {
        return new Game(player, stageManager);
    }

    public TurnResult playTurn(final BattleOption battleOption) {
        if (!player.canPerform(battleOption)) {
            throw new IllegalArgumentException("플레이어가 수행할 수 없는 행동입니다.");
        }
        final int monsterDamageTaken = player.damageFor(battleOption);
        monster.damage(monsterDamageTaken);

        if (!monster.isAlive()) {
            return createTurnResult(battleOption, monsterDamageTaken, 0, false, true);
        }
        final int playerDamageTaken = monster.attack(attackStrategy);
        player.damage(playerDamageTaken);

        return createTurnResult(battleOption, monsterDamageTaken, playerDamageTaken, playerDamageTaken > 0, false);
    }

    private TurnResult createTurnResult(final BattleOption battleOption,
                                        final int monsterDamageTaken,
                                        final int playerDamageTaken,
                                        final boolean monsterAttacked,
                                        final boolean monsterDefeated) {
        return TurnResult.of(
                battleOption,
                player,
                monster,
                monsterDamageTaken,
                playerDamageTaken,
                monsterAttacked,
                monsterDefeated
        );
    }

    public boolean isOver() {
        return !player.isAlive() && monster.isAlive();
    }

    public int currentStage() {
        return stage.value();
    }

    public void nextStage() {
        if (monster.isAlive()) {
            throw new IllegalStateException("스테이지를 클리어해야 다음 스테이지로 이동할 수 있습니다.");
        }

        stage.increment();
        monster = stageManager.create(stage);
    }
}
