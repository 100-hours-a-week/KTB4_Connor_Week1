package org.example.view.in;

import org.example.model.vo.BattleOption;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Set;

import static org.example.dto.GameMenuOption.EXIT;
import static org.example.dto.GameMenuOption.PLAY;
import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.DEFEND;
import static org.example.model.vo.BattleOption.SKILL;
import static org.example.model.vo.JobOption.MAGE;
import static org.example.model.vo.JobOption.WARRIOR;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InputViewTest {

    private InputView inputView(final String input) {
        return new InputView(new BufferedReader(new StringReader(input)));
    }

    @Test
    void 메뉴를_선택한다() {
        assertAll(
                () -> assertEquals(PLAY, inputView("1\n").inputMenuOption()),
                () -> assertEquals(EXIT, inputView("2\n").inputMenuOption())
        );
    }

    @Test
    void 메뉴_번호가_아니면_예외가_발생한다() {
        // expected
        assertThatThrownBy(() -> inputView("3\n").inputMenuOption())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 직업을_선택한다() {
        assertAll(
                () -> assertEquals(WARRIOR, inputView("1\n").inputJobOption()),
                () -> assertEquals(MAGE, inputView("2\n").inputJobOption())
        );
    }

    @Test
    void 직업_번호가_아니면_예외가_발생한다() {
        // expected
        assertThatThrownBy(() -> inputView("3\n").inputJobOption())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 전투_행동을_선택한다() {
        Set<BattleOption> options = Set.of(ATTACK, DEFEND, SKILL);

        assertAll(
                () -> assertEquals(ATTACK, inputView("1\n").inputBattleOption(options)),
                () -> assertEquals(DEFEND, inputView("2\n").inputBattleOption(options)),
                () -> assertEquals(SKILL, inputView("3\n").inputBattleOption(options))
        );
    }

    @Test
    void 전투_행동_번호가_아니면_예외가_발생한다() {
        // given
        Set<BattleOption> options = Set.of(ATTACK, DEFEND, SKILL);

        // expected
        assertThatThrownBy(() -> inputView("4\n").inputBattleOption(options))
                .isInstanceOf(IllegalStateException.class);
    }
}
