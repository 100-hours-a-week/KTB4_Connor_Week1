package org.example.controller;

import org.example.model.Mage;
import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.Warrior;
import org.example.model.vo.BattleOption;
import org.example.model.vo.JobOption;
import org.example.model.vo.MenuOption;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

import java.util.Arrays;
import java.util.stream.Stream;

public class GameController {
    private final InputView inputView;
    private final OutputView outputView;

    public GameController(final InputView inputView,
                          final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
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
            Monster monster = new Monster(stage);
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

            BattleOption battleOption = selectBattleOption();
            boolean defended = battleOption == BattleOption.DEFEND;

            switch (battleOption) {
                case ATTACK -> {
                    monster.receiveDamage(player.attack());
                    outputView.print(player.name() + "의 공격! " + player.attack() + "의 피해를 입혔습니다.");
                }
                case DEFEND -> outputView.print(player.name() + "가 방어 자세를 취했습니다.");
                case SKILL -> {
                    int skillDamage = player.attack() * 2;
                    monster.receiveDamage(skillDamage);
                    outputView.print(player.name() + "의 스킬! " + skillDamage + "의 피해를 입혔습니다.");
                }
            }

            if (!monster.isAlive()) {
                return true;
            }

            int monsterDamage = defended ? Math.max(1, monster.attack() / 2) : monster.attack();
            player.receiveDamage(monsterDamage);
            outputView.print(monster.name() + "의 공격! " + monsterDamage + "의 피해를 입었습니다.");
        }

        return player.isAlive();
    }

    private MenuOption selectMenu() {
        while (true) {
            outputView.print("메인 메뉴");

            Stream.of(MenuOption.values())
                    .map(MenuOption::label)
                    .forEach(outputView::print);

            outputView.print("번호를 입력하세요.");

            String input = inputView.input();

            try {
                int value = Integer.parseInt(input);
                return Arrays.stream(MenuOption.values())
                        .filter(option -> option.number() == value)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
            } catch (NumberFormatException ignored) {
                throw new IllegalArgumentException("잘못된 입력입니다.");
            }
        }
    }

    private Player createPlayer() {
        return switch (selectJob()) {
            case WARRIOR -> new Warrior();
            case MAGE -> new Mage();
        };
    }

    private BattleOption selectBattleOption() {
        while (true) {
            outputView.print("행동 선택");

            Stream.of(BattleOption.values())
                    .map(option -> option.number() + ". " + option.label())
                    .forEach(outputView::print);

            outputView.print("번호를 입력하세요.");

            String input = inputView.input();

            try {
                int value = Integer.parseInt(input);
                return Arrays.stream(BattleOption.values())
                        .filter(option -> option.number() == value)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
            } catch (NumberFormatException ignored) {
                throw new IllegalArgumentException("잘못된 입력입니다.");
            }
        }
    }

    private JobOption selectJob() {
        while (true) {
            outputView.print("직업 선택");

            Stream.of(JobOption.values())
                    .map(JobOption::label)
                    .forEach(outputView::print);

            outputView.print("번호를 입력하세요.");

            String input = inputView.input();

            try {
                int value = Integer.parseInt(input);
                return Arrays.stream(JobOption.values())
                        .filter(option -> option.number() == value)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("잘못된 입력입니다."));
            } catch (NumberFormatException ignored) {
                throw new IllegalArgumentException("잘못된 입력입니다.");
            }
        }
    }

    private void showJobDescriptions() {
        // 직업 설명
    }

    private void printStatus(Player player, Monster monster) {
        outputView.print("====================");
        outputView.print(player.name() + " HP: " + player.hp() + "/" + player.maxHp());
        outputView.print(monster.name() + " HP: " + monster.hp());
        outputView.print("====================");
    }


}
