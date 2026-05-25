package org.example.controller;

import org.example.dto.GameMenuOption;
import org.example.model.Game;
import org.example.model.MonsterFactory;
import org.example.model.Player;
import org.example.model.PlayerFactory;
import org.example.model.vo.BattleOption;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

public class GameController {
    private static final long DEFAULT_MONSTER_ATTACK_INTERVAL_MILLIS = 2_000L;

    private final InputView inputView;
    private final OutputView outputView;
    private final PlayerFactory playerFactory;
    private final MonsterFactory stageManager;
    private final long monsterAttackIntervalMillis;

    public GameController(final InputView inputView,
                          final OutputView outputView) {
        this(inputView, outputView, new PlayerFactory(), new MonsterFactory(), DEFAULT_MONSTER_ATTACK_INTERVAL_MILLIS);
    }


    GameController(final InputView inputView,
                   final OutputView outputView,
                   final MonsterFactory stageManager,
                   final long monsterAttackIntervalMillis) {
        this(inputView, outputView, new PlayerFactory(), stageManager, monsterAttackIntervalMillis);
    }

    private GameController(final InputView inputView,
                           final OutputView outputView,
                           final PlayerFactory playerFactory,
                           final MonsterFactory stageManager,
                           final long monsterAttackIntervalMillis) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.playerFactory = playerFactory;
        this.stageManager = stageManager;
        this.monsterAttackIntervalMillis = monsterAttackIntervalMillis;
    }

    public void run() {
        final Game game = initGame();
        if (game == null) {
            return;
        }

        while (!game.isOver()) {
            outputView.printStageStart(game.currentStage());

            final Thread monsterAttackThread = startMonsterAttackThread(game);
            try {
                while (!game.isOver() && !game.isClear()) {
                    final Player player = game.player();
                    final BattleOption option = inputView.inputBattleOption(player.availableBattleOptions());

                    game.playTurn(option).ifPresent(outputView::printTurnResult);
                }
            } finally {
                stopMonsterAttackThread(monsterAttackThread);
            }

            if (game.isOver()) {
                outputView.printGameOver();
                return;
            }

            outputView.printStageClear(game.monster().name());
            game.nextStage();
        }
    }

    private Thread startMonsterAttackThread(final Game game) {
        Thread thread = new Thread(() -> attackAutomatically(game), "monster-attack");
        thread.start();
        return thread;
    }

    private void attackAutomatically(final Game game) {
        // NOTE: ScheduledExecutorService으로 스케줄링 관리 개선할 수 있을 것 같음
        try {
            while (!game.isOver() && !game.isClear()) {
                Thread.sleep(monsterAttackIntervalMillis);
                game.automaticAttack().ifPresent(outputView::printMonsterAttackResult);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void stopMonsterAttackThread(final Thread monsterAttackThread) {
        monsterAttackThread.interrupt();
        try {
            monsterAttackThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Game initGame() {
        final GameMenuOption menu = inputView.inputMenuOption();
        return switch (menu) {
            case PLAY -> {
                final Player player = playerFactory.create(inputView.inputJobOption());
                yield Game.start(player, stageManager);
            }
            case EXIT -> {
                outputView.printExit();
                yield null;
            }
        };
    }
}
