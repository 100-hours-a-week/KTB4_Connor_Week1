package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.example.model.vo.BattleOption.*;
import static org.example.model.vo.JobOption.MAGE;
import static org.example.model.vo.JobOption.WARRIOR;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void 직업_옵션으로_플레이어를_생성한다() {
        assertAll(
                () -> assertInstanceOf(Warrior.class, Player.from(WARRIOR)),
                () -> assertInstanceOf(Mage.class, Player.from(MAGE))
        );
    }

    @Test
    void 플레이어의_행동_목록은_수정할_수_없다() {
        Warrior warrior = new Warrior();

        assertThrows(UnsupportedOperationException.class,
                () -> warrior.availableBattleOptions().add(SKILL));
    }

    @Test
    void null_행동은_수행할_수_없다() {
        assertFalse(new Warrior().canPerform(null));
    }

    @Test
    void 행동에_따라_피해량이_달라진다() {
        Warrior warrior = new Warrior();
        Mage mage = new Mage();

        assertAll(
                () -> assertEquals(25, warrior.damageFor(ATTACK)),
                () -> assertEquals(0, warrior.damageFor(DEFEND)),
                () -> assertEquals(80, mage.damageFor(SKILL))
        );
    }

    @Test
    void 수행할_수_없는_행동의_피해량은_계산할_수_없다() {
        assertThrows(IllegalArgumentException.class, () -> new Warrior().damageFor(SKILL));
    }
}
