package org.example.controller;

import org.example.dto.TurnResult;
import org.example.dto.GameMenuOption;
import org.example.engine.BattleTurnResult;
import org.example.engine.MonsterFactory;
import org.example.model.Game;
import org.example.model.Player;
import org.example.model.vo.BattleOption;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

public class GameController {
    private final InputView inputView;
    private final OutputView outputView;
    private final MonsterFactory stageManager;

    public GameController(final InputView inputView,
                          final OutputView outputView) {
        this(inputView, outputView, new MonsterFactory());
    }

    public GameController(final InputView inputView,
                          final OutputView outputView,
                          final MonsterFactory stageManager) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.stageManager = stageManager;
    }

    public void run() {
        final Game game = initGame();
        if (game == null) {
            return;
        }

        while (!game.isOver()) {
            outputView.printStageStart(game.stage().value());

            while (!game.isOver() && game.monster().isAlive()) {
                final Player player = game.player();
                final BattleOption option = inputView.inputBattleOption(player.availableBattleOptions());

                final BattleTurnResult result = game.playTurn(option);
                outputView.printBattleResult(TurnResult.from(player, game.monster(), result));
            }

            if (!game.player().isAlive()) {
                outputView.printGameOver();
                return;
            }

            outputView.printStageClear(game.monster().name());
            game.nextStage();
        }
    }

    private Game initGame() {
        final GameMenuOption menu = inputView.inputMenuOption();
        return switch (menu) {
            case PLAY -> {
                final Player player = Player.from(inputView.inputJobOption());
                yield Game.start(player, stageManager);
            }
            case EXIT -> {
                outputView.printExit();
                yield null;
            }
        };
    }
}
