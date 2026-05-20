package org.example.view.out;

import org.example.dto.TurnResult;

import java.io.PrintStream;
import java.util.Objects;
import java.util.StringJoiner;

public class OutputView {
    private static final String LINE = "==============================";

    private final PrintStream printStream;

    public OutputView() {
        this(System.out);
    }

    public OutputView(final PrintStream printStream) {
        this.printStream = Objects.requireNonNull(printStream);
    }

    public void print(final String message) {
        printStream.println(message);
    }

    public void printExit() {
        print("게임을 종료합니다.");
    }

    public void printStageStart(final int stage) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("")
                .add(LINE)
                .add(stage + " 스테이지를 시작합니다.")
                .add(LINE);

        print(joiner.toString());
    }

    public void printGameOver() {
        print("\n플레이어가 패배했습니다. 게임 오버!");
    }

    public void printStageClear(final String monsterName) {
        print("\n스테이지 클리어! 처치한 몬스터: " + monsterName);
    }

    public void printBattleResult(final TurnResult result) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("")
                .add("[전투 결과]")
                .add(playerActionMessage(result));

        if (result.monsterDefeated()) {
            joiner.add("몬스터 처치: " + result.monsterName());
            print(joiner.toString());
            return;
        }

        joiner.add(monsterActionMessage(result))
                .add("현재 체력")
                .add("- " + result.playerName() + ": " + result.playerHp() + "/" + result.playerMaxHp())
                .add("- " + result.monsterName() + ": " + result.monsterHp());

        print(joiner.toString());
    }

    private String playerActionMessage(final TurnResult result) {
        return switch (result.playerAction()) {
            case ATTACK -> result.playerName() + "의 공격 - "
                    + result.monsterName() + "에게 "
                    + result.monsterDamageTaken() + " 피해";
            case DEFEND -> result.playerName() + "는 방어 자세를 취했습니다.";
            case SKILL -> result.playerName() + "의 스킬 - "
                    + result.monsterName() + "에게 "
                    + result.monsterDamageTaken() + " 피해";
        };
    }

    private String monsterActionMessage(final TurnResult result) {
        if (result.monsterAttacked()) {
            return result.monsterName() + "의 반격 - "
                    + result.playerName() + "에게 "
                    + result.playerDamageTaken() + " 피해";
        }

        return result.monsterName() + "는 공격하지 못했습니다.";
    }
}
