package org.example.model;

import org.example.model.vo.BattleOption;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.model.vo.BattleOption.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void 전사를_생성한다() {
        // given
        Warrior warrior = new Warrior();

        // expected
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
        // given
        Mage mage = new Mage();

        // expected
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
        // expected
        assertThatThrownBy(this::createInvalidPlayer)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 플레이어의_행동_목록은_수정할_수_없다() {
        // given
        Warrior warrior = new Warrior();
        Set<BattleOption> availableBattleOptions = warrior.availableBattleOptions();

        // expected
        assertThatThrownBy(() -> availableBattleOptions.add(SKILL))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void null_행동은_수행할_수_없다() {
        // expected
        assertFalse(new Warrior().canPerform(null));
    }

    @Test
    void 행동에_따라_피해량이_달라진다() {
        // given
        Warrior warrior = new Warrior();
        Mage mage = new Mage();

        // expected
        assertAll(
                () -> assertEquals(25, warrior.damageFor(ATTACK)),
                () -> assertEquals(0, warrior.damageFor(DEFEND)),
                () -> assertEquals(80, mage.damageFor(SKILL))
        );
    }

    @Test
    void 수행할_수_없는_행동의_피해량은_계산할_수_없다() {
        // given
        Warrior warrior = new Warrior();

        // expected
        assertThatThrownBy(() -> warrior.damageFor(SKILL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void createInvalidPlayer() {
        new Player("테스트", 100, 10, Set.of()) {
            @Override
            protected int damageForAvailableAction(final BattleOption battleOption) {
                return 0;
            }
        };
    }
}
