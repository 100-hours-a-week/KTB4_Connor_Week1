package model;

import org.example.model.Mage;
import org.example.model.Player;
import org.example.model.Warrior;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.DEFEND;
import static org.example.model.vo.BattleOption.SKILL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    @Test
    void 전사를_생성한다() {
        Warrior warrior = new Warrior();

        assertAll(
                () -> assertEquals("Warrior (전사)", warrior.name()),
                () -> assertEquals(180, warrior.hp()),
                () -> assertEquals(25, warrior.attack()),
                () -> assertTrue(warrior.canPerform(ATTACK)),
                () -> assertTrue(warrior.canPerform(DEFEND)),
                () -> assertFalse(warrior.canPerform(SKILL))
        );
    }

    @Test
    void 마법사를_생성한다() {
        Mage mage = new Mage();

        assertAll(
                () -> assertEquals("Mage (메이지)", mage.name()),
                () -> assertEquals(120, mage.hp()),
                () -> assertEquals(40, mage.attack()),
                () -> assertTrue(mage.canPerform(ATTACK)),
                () -> assertFalse(mage.canPerform(DEFEND)),
                () -> assertTrue(mage.canPerform(SKILL))
        );
    }

    @Test
    void 플레이어는_하나_이상의_행동을_가져야_한다() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("테스트", 100, 10, Set.of()) {
                });
    }
}
