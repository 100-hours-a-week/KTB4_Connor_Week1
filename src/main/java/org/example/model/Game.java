package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.example.model.vo.BattleOption;

import java.util.Objects;
import java.util.Random;

@Getter
@Accessors(fluent = true)
public class Game {
    private final Player player;
    private final StageManager stageManager;
    private final Random random;
    private final Stage stage;
    private Monster monster;

    private Game(final Player player,
                 final StageManager stageManager) {
        this.player = Objects.requireNonNull(player);
        this.stageManager = Objects.requireNonNull(stageManager);
        this.random = new Random();
        this.stage = new Stage(1);
        this.monster = stageManager.createMonster(stage);
    }

    public static Game start(final Player player,
                             final StageManager stageManager) {
        return new Game(player, stageManager);
    }

    public BattleTurnResult playTurn(final BattleOption battleOption) {
        if (!isStageInProgress()) {
            throw new IllegalStateException("진행 중인 스테이지가 없습니다.");
        }

        if (!player.canPerform(battleOption)) {
            throw new IllegalArgumentException("플레이어가 수행할 수 없는 행동입니다.");
        }

        int monsterDamageTaken = monster.receiveDamage(player.damageFor(battleOption));

        if (!monster.isAlive()) {
            return new BattleTurnResult(battleOption, monsterDamageTaken, 0, false, true, false);
        }

        boolean monsterAttacked = monster.shouldAttack(random.nextInt(100));
        int playerDamageTaken = 0;

        if (monsterAttacked) {
            int monsterDamage = battleOption == BattleOption.DEFEND
                    ? Math.max(1, monster.attack() / 2)
                    : monster.attack();
            playerDamageTaken = player.receiveDamage(monsterDamage);
        }

        return new BattleTurnResult(battleOption,
                monsterDamageTaken,
                playerDamageTaken,
                monsterAttacked,
                false,
                !player.isAlive()
        );
    }

    public boolean isOver() {
        return !player.isAlive();
    }

    public boolean isStageInProgress() {
        return player.isAlive() && monster.isAlive();
    }

    public void proceedNextStage() {
        if (monster.isAlive()) {
            throw new IllegalStateException("스테이지를 클리어해야 다음 스테이지로 이동할 수 있습니다.");
        }

        stage.increment();
        monster = stageManager.createMonster(stage);
    }
}
