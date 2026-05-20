package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.engine.BattleTurnResult;
import org.example.engine.MonsterFactory;
import org.example.model.vo.BattleOption;

import java.util.Objects;
import java.util.Random;

@Getter
@Accessors(fluent = true)
public class Game {
    private final Player player;
    private final MonsterFactory stageManager;
    private final Random random;
    private final Stage stage;
    private final AttackStrategy attackStrategy;
    private Monster monster;

    private Game(final Player player,
                 final MonsterFactory stageManager) {
        this.player = Objects.requireNonNull(player);
        this.stageManager = Objects.requireNonNull(stageManager);
        this.random = new Random();
        this.stage = new Stage(1);
        this.monster = stageManager.create(stage);
        this.attackStrategy = new RandomAttackStrategy();
    }

    public static Game start(final Player player,
                             final MonsterFactory stageManager) {
        return new Game(player, stageManager);
    }

    public BattleTurnResult playTurn(final BattleOption battleOption) {
        if (!player.canPerform(battleOption)) {
            throw new IllegalArgumentException("플레이어가 수행할 수 없는 행동입니다.");
        }
        int monsterDamageTaken = player.attack(attackStrategy);
        monster.damage(monsterDamageTaken);

        if (!monster.isAlive()) {
            return new BattleTurnResult(battleOption, monsterDamageTaken, 0, false, true, false);
        }
        int playerDamageTaken = monster.attack(attackStrategy);
        player.damage(playerDamageTaken);

        return new BattleTurnResult(battleOption,
                monsterDamageTaken,
                playerDamageTaken,
                playerDamageTaken > 0,
                false,
                !player.isAlive()
        );
    }

    public boolean isOver() {
        return !player.isAlive() && monster.isAlive();
    }

    public void nextStage() {
        if (monster.isAlive()) {
            throw new IllegalStateException("스테이지를 클리어해야 다음 스테이지로 이동할 수 있습니다.");
        }

        stage.increment();
        monster = stageManager.create(stage);
    }
}
