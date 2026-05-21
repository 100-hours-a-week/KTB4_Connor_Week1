package org.example.view.out;

import org.example.dto.TurnResult;
import org.example.model.Monster;
import org.example.model.Player;
import org.example.model.Warrior;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.example.model.vo.BattleOption.ATTACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutputViewTest {

    @Test
    void 메시지를_출력한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputView outputView = new OutputView(new PrintStream(output));

        outputView.print("hello");

        assertEquals("hello" + System.lineSeparator(), output.toString());
    }

    @Test
    void 전투_결과를_출력한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputView outputView = new OutputView(new PrintStream(output));
        Player player = new Warrior();
        Monster monster = new Monster("Lv.1 고블린", 40, 20);
        player.damage(20);
        monster.damage(25);

        TurnResult result = TurnResult.of(
                ATTACK,
                player,
                monster,
                25,
                20,
                true,
                false
        );

        outputView.printTurnResult(result);

        String message = output.toString();
        assertAll(
                () -> assertTrue(message.contains("[전투 결과]")),
                () -> assertTrue(message.contains("Warrior (전사)의 공격 - Lv.1 고블린에게 25 피해")),
                () -> assertTrue(message.contains("Lv.1 고블린의 반격 - Warrior (전사)에게 20 피해")),
                () -> assertTrue(message.contains("Warrior (전사): 160/180")),
                () -> assertTrue(message.contains("Lv.1 고블린: 15"))
        );
    }

    @Test
    void 몬스터를_처치하면_체력_상태는_출력하지_않는다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputView outputView = new OutputView(new PrintStream(output));
        Player player = new Warrior();
        Monster monster = new Monster("Lv.1 고블린", 25, 20);
        monster.damage(25);

        TurnResult result = TurnResult.of(
                ATTACK,
                player,
                monster,
                25,
                0,
                false,
                true
        );

        outputView.printTurnResult(result);

        String message = output.toString();
        assertAll(
                () -> assertTrue(message.contains("몬스터 처치: Lv.1 고블린")),
                () -> assertFalse(message.contains("현재 체력"))
        );
    }

    @Test
    void 출력_대상은_null일_수_없다() {
        assertThrows(NullPointerException.class, () -> new OutputView(null));
    }
}
