package org.example.controller;

import org.example.model.Mage;
import org.example.model.Warrior;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.example.model.vo.BattleOption.DEFEND;
import static org.example.model.vo.BattleOption.SKILL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerTest {

    @Test
    void 잘못된_입력은_같은_메뉴를_다시_보여준다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GameController controller = controllerWithInput("abc\n3\n", output);

        controller.run();

        String message = output.toString();
        assertTrue(message.contains("잘못된 입력입니다."));
        assertEquals(2, countOccurrences(message, "메인 메뉴"));
    }

    @Test
    void 전사는_방어만_선택할_수_있다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GameController controller = controllerWithInput("2\n", output);

        assertEquals(DEFEND, controller.selectBattleOption(new Warrior()));

        String message = output.toString();
        assertTrue(message.contains("1. 공격"));
        assertTrue(message.contains("2. 방어"));
        assertFalse(message.contains("3. 스킬"));
    }

    @Test
    void 마법사는_스킬만_선택할_수_있다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GameController controller = controllerWithInput("3\n", output);

        assertEquals(SKILL, controller.selectBattleOption(new Mage()));

        String message = output.toString();
        assertTrue(message.contains("1. 공격"));
        assertFalse(message.contains("2. 방어"));
        assertTrue(message.contains("3. 스킬"));
    }

    private GameController controllerWithInput(String input, ByteArrayOutputStream output) {
        return new GameController(
                new InputView(new Scanner(input)),
                new OutputView(new PrintStream(output))
        );
    }

    private int countOccurrences(String source, String target) {
        int count = 0;
        int fromIndex = 0;

        while (true) {
            int index = source.indexOf(target, fromIndex);
            if (index < 0) {
                return count;
            }
            count++;
            fromIndex = index + target.length();
        }
    }
}
