package org.example.view.in;

import org.example.dto.GameMenuOption;
import org.example.model.vo.BattleOption;
import org.example.model.vo.JobOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.StringJoiner;

import static org.example.dto.GameMenuOption.EXIT;
import static org.example.dto.GameMenuOption.PLAY;
import static org.example.model.vo.JobOption.MAGE;
import static org.example.model.vo.JobOption.WARRIOR;

public class InputView {
    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    private static String readLine() {
        try {
            return READER.readLine();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public GameMenuOption inputMenuOption() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("메뉴를 선택하세요.");
        for (GameMenuOption option : GameMenuOption.values()) {
            joiner.add(option.number() + ". " + option.label());
        }
        System.out.println(joiner);

        final int input = Integer.parseInt(readLine());

        return switch (input) {
            case 1 -> PLAY;
            case 2 -> EXIT;
            default -> throw new IllegalStateException("1 또는 2를 입력해야 합니다.");
        };
    }

    public JobOption inputJobOption() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("직업 선택하세요.");
        for (JobOption option : JobOption.values()) {
            joiner.add(option.number() + ". " + option.label());
        }
        System.out.println(joiner);

        final int input = Integer.parseInt(readLine());

        return switch (input) {
            case 1 -> WARRIOR;
            case 2 -> MAGE;
            default -> throw new IllegalStateException("1 또는 2를 입력해야 합니다.");
        };
    }

    public BattleOption inputBattleOption(Set<BattleOption> options) {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("행동을 선택하세요.");
        for (BattleOption option : options) {
            joiner.add(option.number() + ". " + option.label());
        }
        System.out.println(joiner);

        final int input = Integer.parseInt(readLine());

        return switch (input) {
            case 1 -> BattleOption.ATTACK;
            case 2 -> BattleOption.DEFEND;
            case 3 -> BattleOption.SKILL;
            default -> throw new IllegalStateException("1, 2 또는 3을 입력해야 합니다.");
        };
    }
}
