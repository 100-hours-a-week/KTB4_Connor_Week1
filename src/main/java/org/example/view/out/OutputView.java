package org.example.view.out;

import org.example.dto.TurnResult;

import java.io.PrintStream;
import java.util.Objects;
import java.util.StringJoiner;

public class OutputView implements AutoCloseable {

    private static final String LINE = "==============================";

    private final PrintStream printStream;
    private final AnimationPlayer animationPlayer;

    public OutputView() {
        this(System.out);
    }

    public OutputView(final PrintStream printStream) {
        this(printStream, new AnimationPlayer(printStream, printStream == System.out));
    }

    OutputView(final PrintStream printStream,
               final AnimationPlayer animationPlayer) {
        this.printStream = Objects.requireNonNull(printStream);
        this.animationPlayer = Objects.requireNonNull(animationPlayer);
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
        printActionAnimation("dead");
        print("\n스테이지 클리어! 처치한 몬스터: " + monsterName);
    }

    public void printTurnResult(final TurnResult result) {
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
        StringBuilder builder = new StringBuilder();
        return switch (result.playerAction()) {
            case ATTACK -> {
                if (result.monsterDamageTaken() > 0) {
                    printActionAnimation("damage");
                } else {
                    printActionAnimation("avoid");
                }
                yield builder.append(result.playerName()).append("의 공격 - ")
                        .append(result.monsterName()).append("에게 ")
                        .append(result.monsterDamageTaken()).append(" 피해")
                        .toString();
            }
            case DEFEND -> {
                printActionAnimation("default");
                yield builder.append(result.playerName()).append("는 방어 자세를 취했습니다.")
                        .toString();
            }
            case SKILL -> {
                if (result.monsterDamageTaken() > 0) {
                    printActionAnimation("damage");
                } else {
                    printActionAnimation("avoid");
                }
                yield builder.append(result.playerName()).append("의 스킬 - ")
                        .append(result.monsterName()).append("에게 ")
                        .append(result.monsterDamageTaken()).append(" 피해")
                        .toString();
            }
        };
    }

    private String monsterActionMessage(final TurnResult result) {
        if (result.monsterAttacked()) {
            printActionAnimation("attack");
            return result.monsterName() + "의 반격 - "
                    + result.playerName() + "에게 "
                    + result.playerDamageTaken() + " 피해";
        }

        printActionAnimation("avoid");
        return result.monsterName() + "의 공격를 피했습니다.";
    }

    private void printActionAnimation(final String action) {
        animationPlayer.play(action).join();
    }

    @Override
    public void close() {
        animationPlayer.shutdown();
    }
}
