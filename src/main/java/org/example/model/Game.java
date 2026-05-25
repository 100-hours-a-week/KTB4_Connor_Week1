package org.example.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.example.dto.MonsterAttackResult;
import org.example.dto.TurnResult;
import org.example.model.vo.BattleOption;

import java.util.Objects;
import java.util.Optional;

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

    public synchronized Optional<TurnResult> playTurn(final BattleOption battleOption) {
        if (isOver() || isClear()) {
            return Optional.empty();
        }
        if (!player.canPerform(battleOption)) {
            throw new IllegalArgumentException("플레이어가 수행할 수 없는 행동입니다.");
        }

        final int monsterDamageTaken = player.damageFor(battleOption);
        monster.damage(monsterDamageTaken);

        return Optional.of(createTurnResult(battleOption, monsterDamageTaken, !monster.isAlive()));
    }

    public synchronized Optional<MonsterAttackResult> automaticAttack() {
        if (isOver() || isClear()) {
            return Optional.empty();
        }

        final int playerDamageTaken = monster.attack(attackStrategy);
        player.damage(playerDamageTaken);

        return Optional.of(MonsterAttackResult.of(player, monster, playerDamageTaken));
    }

    private TurnResult createTurnResult(final BattleOption battleOption,
                                        final int monsterDamageTaken,
                                        final boolean monsterDefeated) {
        return TurnResult.of(
                battleOption,
                player,
                monster,
                monsterDamageTaken,
                monsterDefeated
        );
    }

    public synchronized boolean isOver() {
        return !player.isAlive();
    }

    public synchronized boolean isClear() {
        return !monster.isAlive();
    }

    public int currentStage() {
        return stage.value();
    }

    public synchronized void nextStage() {
        if (monster.isAlive()) {
            throw new IllegalStateException("스테이지를 클리어해야 다음 스테이지로 이동할 수 있습니다.");
        }

        stage.increment();
        monster = stageManager.create(stage);
    }
}
