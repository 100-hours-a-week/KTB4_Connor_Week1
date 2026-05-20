package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.engine.BattleEngine;
import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.example.model.vo.BattleOption;

import java.util.Objects;

@Getter
@Accessors(fluent = true)
public class Game {
    private final Player player;
    private final StageManager stageManager;
    private final BattleEngine battleEngine;

    private final Stage stage;
    private Monster monster;

    private Game(final Player player,
                 final StageManager stageManager,
                 final BattleEngine battleEngine) {
        this.player = Objects.requireNonNull(player);
        this.stageManager = Objects.requireNonNull(stageManager);
        this.battleEngine = Objects.requireNonNull(battleEngine);
        this.stage = new Stage(1);
        this.monster = stageManager.createMonster(stage);
    }

    public static Game start(final Player player,
                             final StageManager stageManager,
                             final BattleEngine battleEngine) {
        return new Game(player, stageManager, battleEngine);
    }

    public BattleTurnResult playTurn(final BattleOption battleOption) {
        if (!isStageInProgress()) {
            throw new IllegalStateException("진행 중인 스테이지가 없습니다.");
        }

        return battleEngine.resolveTurn(player, monster, battleOption);
    }

    public boolean isOver() {
        return !player.isAlive();
    }

    public boolean isStageInProgress() {
        return player.isAlive() && monster.isAlive();
    }

    public boolean isStageCleared() {
        return !monster.isAlive();
    }

    public void proceedNextStage() {
        if (!isStageCleared()) {
            throw new IllegalStateException("스테이지를 클리어해야 다음 스테이지로 이동할 수 있습니다.");
        }

        stage.increment();
        monster = stageManager.createMonster(stage);
    }
}
