package org.example.controller;

import org.example.dto.GameMenuOption;
import org.example.engine.BattleEngine;
import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.example.model.Game;
import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.vo.BattleOption;
import org.example.model.vo.JobOption;
import org.example.model.vo.Option;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

import java.util.Arrays;

public class GameController {
    private static final String INVALID_INPUT_MESSAGE = "잘못된 입력입니다.";

    private final InputView inputView;
    private final OutputView outputView;
    private final BattleEngine battleEngine;
    private final StageManager stageManager;

    public GameController(final InputView inputView,
                          final OutputView outputView) {
        this(inputView, outputView, new BattleEngine(), new StageManager());
    }

    public GameController(final InputView inputView,
                          final OutputView outputView,
                          final BattleEngine battleEngine,
                          final StageManager stageManager) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.battleEngine = battleEngine;
        this.stageManager = stageManager;
    }

    public void run() {
        try {
            final GameMenuOption menu = selectMenuOption();

            switch (menu) {
                case PLAY -> play();
                case EXIT -> outputView.print("게임을 종료합니다.");
            }
        } catch (IllegalArgumentException ignored) {
            // 더 이상 읽을 입력이 없으면 조용히 종료한다.
        }
    }

    private void play() {
        final Player player = Player.from(selectJobOption());
        final Game game = Game.start(player, stageManager, battleEngine);

        outputView.print(player.name() + "를 선택했습니다.");

        while (game.isRunning()) {
            outputView.print(game.stage() + " 스테이지를 시작합니다.");

            battle(game);

            if (!game.player().isAlive()) {
                outputView.print("플레이어가 패배했습니다. 게임 오버!");
                return;
            }

            outputView.print(game.monster().name() + "를 처치했습니다.");
            game.proceedNextStage();
        }
    }

    private void battle(final Game game) {
        while (game.isStageInProgress()) {
            printStatus(game.player(), game.monster());

            final BattleTurnResult result = game.playTurn(selectBattleOption(game.player()));
            printBattleResult(game.player(), game.monster(), result);
        }
    }

    private GameMenuOption selectMenuOption() {
        return promptSelection("메인 메뉴", GameMenuOption.values());
    }

    private JobOption selectJobOption() {
        return promptSelection("직업 선택", JobOption.values());
    }

    BattleOption selectBattleOption(final Player player) {
        final BattleOption[] availableOptions = Arrays.stream(BattleOption.values())
                .filter(player::canPerform)
                .toArray(BattleOption[]::new);
        return promptSelection("행동 선택", availableOptions);
    }

    private <T extends Option> T promptSelection(final String title, final T[] options) {
        while (true) {
            printOptions(title, options);

            try {
                return findOption(options, inputView.inputNumber());
            } catch (IllegalArgumentException e) {
                if ("입력이 없습니다.".equals(e.getMessage())) {
                    throw e;
                }

                outputView.print(INVALID_INPUT_MESSAGE);
                if (!inputView.canRetry()) {
                    throw new IllegalArgumentException("입력이 없습니다.");
                }
            }
        }
    }

    private <T extends Option> void printOptions(final String title, final T[] options) {
        outputView.print(title);
        for (T option : options) {
            outputView.print(option.displayText());
        }
    }

    private <T extends Option> T findOption(final T[] options, final int input) {
        return Arrays.stream(options)
                .filter(option -> option.number() == input)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("선택할 수 없는 옵션입니다."));
    }

    private void printStatus(final Player player, final Monster monster) {
        outputView.print("====================");
        outputView.print(player.name() + " HP: " + player.hp() + "/" + player.maxHp());
        outputView.print(monster.name() + " HP: " + monster.hp());
        outputView.print("====================");
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
    }
}
