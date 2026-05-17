package org.example.controller;

import org.example.engine.BattleEngine;
import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.example.model.Mage;
import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.Warrior;
import org.example.model.vo.BattleOption;
import org.example.model.vo.JobOption;
import org.example.model.vo.MenuOption;
import org.example.model.vo.Option;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameController {
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
        while (true) {
            switch (selectMenu()) {
                case PLAY -> play();
                case DESCRIPTION -> showJobDescriptions();
                case EXIT -> { return; }
            }
        }
    }

    private void play() {
        Player player = createPlayer();
        int stage = 1;

        outputView.print(player.name() + "를 선택했습니다.");

        while (player.isAlive()) {
            Monster monster = stageManager.createMonster(stage);
            outputView.print(stage + " 스테이지를 시작합니다.");

            if (!battle(player, monster)) {
                outputView.print("플레이어가 패배했습니다. 게임 오버!");
                return;
            }

            outputView.print(monster.name() + "를 처치했습니다.");
            stage++;
        }
    }

    private boolean battle(Player player, Monster monster) {
        while (player.isAlive() && monster.isAlive()) {
            printStatus(player, monster);

            BattleTurnResult result = battleEngine.resolveTurn(player, monster, selectBattleOption(player));
            printBattleResult(player, monster, result);

            if (result.monsterDefeated()) {
                return true;
            }
        }

        return player.isAlive();
    }

    private MenuOption selectMenu() {
        return promptSelection("메인 메뉴", MenuOption.values());
    }

    private Player createPlayer() {
        return switch (selectJob()) {
            case WARRIOR -> new Warrior();
            case MAGE -> new Mage();
        };
    }

    BattleOption selectBattleOption(Player player) {
        BattleOption[] availableOptions = Arrays.stream(BattleOption.values())
                .filter(player::canPerform)
                .toArray(BattleOption[]::new);
        return promptSelection("행동 선택", availableOptions);
    }

    private JobOption selectJob() {
        return promptSelection("직업 선택", JobOption.values());
    }

    private void showJobDescriptions() {
        outputView.print("직업 설명");
        List.of(new Warrior(), new Mage())
                .forEach(player -> outputView.print(formatJobDescription(player)));
    }

    private void printStatus(Player player, Monster monster) {
        outputView.print("====================");
        outputView.print(player.name() + " HP: " + player.hp() + "/" + player.maxHp());
        outputView.print(monster.name() + " HP: " + monster.hp());
        outputView.print("====================");
    }

    private void printBattleResult(Player player, Monster monster, BattleTurnResult result) {
        switch (result.playerAction()) {
            case ATTACK -> outputView.print(player.name() + "의 공격! " + result.monsterDamageTaken() + "의 피해를 입혔습니다.");
            case DEFEND -> outputView.print(player.name() + "가 방어 자세를 취했습니다.");
            case SKILL -> outputView.print(player.name() + "의 스킬! " + result.monsterDamageTaken() + "의 피해를 입혔습니다.");
        }

        if (!result.monsterDefeated()) {
            outputView.print(monster.name() + "의 공격! " + result.playerDamageTaken() + "의 피해를 입었습니다.");
        }
    }

    private String formatJobDescription(Player player) {
        String availableActions = Arrays.stream(BattleOption.values())
                .filter(player::canPerform)
                .map(BattleOption::label)
                .reduce((first, second) -> first + ", " + second)
                .orElse("-");

        return player.name()
                + " - HP: " + player.maxHp()
                + ", 공격력: " + player.attack()
                + ", 행동: " + availableActions;
    }

    private <T extends Enum<T> & Option> T promptSelection(String title, T[] options) {
        while (true) {
            outputView.print(title);
            Stream.of(options)
                    .map(Option::displayText)
                    .forEach(outputView::print);
            outputView.print("번호를 입력하세요.");

            String input = inputView.input();

            try {
                int value = Integer.parseInt(input);
                return Arrays.stream(options)
                        .filter(option -> option.number() == value)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
            } catch (IllegalArgumentException ignored) {
                outputView.print("잘못된 입력입니다.");
            }
        }
    }

}
