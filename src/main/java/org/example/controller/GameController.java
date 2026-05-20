package org.example.controller;

import org.example.dto.GameMenuOption;
import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.example.model.*;
import org.example.model.vo.BattleOption;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

public class GameController {
    private final InputView inputView;
    private final OutputView outputView;
    private final StageManager stageManager;

    public GameController(final InputView inputView,
                          final OutputView outputView) {
        this(inputView, outputView, new StageManager());
    }

    public GameController(final InputView inputView,
                          final OutputView outputView,
                          final StageManager stageManager) {
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
            outputView.print(game.stage().value() + " 스테이지를 시작합니다.");

            while (!game.isOver()) {
                final Player player = game.player();
                final BattleOption option = inputView.inputBattleOption(player.availableBattleOptions());

                final BattleTurnResult result = game.playTurn(option);
                printBattleResult(player, game.monster(), result);
            }

            if (!game.player().isAlive()) {
                outputView.print("플레이어가 패배했습니다. 게임 오버!");
                return;
            }

            outputView.print(game.monster().name() + "를 처치했습니다.");
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
                outputView.print("게임을 종료합니다.");
                yield null;
            }
        };
    }

    private void printBattleResult(final Player player, final Monster monster, final BattleTurnResult result) {
        switch (result.playerAction()) {
            case ATTACK -> outputView.print(player.name() + "의 공격! " + result.monsterDamageTaken() + "의 피해를 입혔습니다.");
            case DEFEND -> outputView.print(player.name() + "가 방어 자세를 취했습니다.");
            case SKILL -> outputView.print(player.name() + "의 스킬! " + result.monsterDamageTaken() + "의 피해를 입혔습니다.");
        }

        if (result.monsterDefeated()) {
            return;
        }

        if (result.monsterAttacked()) {
            outputView.print(monster.name() + "의 공격! " + result.playerDamageTaken() + "의 피해를 입었습니다.");
            return;
        }

        outputView.print(monster.name() + "가 공격하지 않았습니다.");
        outputView.print("====================");
        outputView.print(player.name() + " HP: " + player.hp() + "/" + player.maxHp());
        outputView.print(monster.name() + " HP: " + monster.hp());
        outputView.print("====================");
    }
}
