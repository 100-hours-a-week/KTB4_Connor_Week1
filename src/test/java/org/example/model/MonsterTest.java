package org.example.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MonsterTest {

    @Test
    void 몬스터를_생성한다() {
        Monster monster = new Monster("고블린", 40, 20);

        assertAll(
                () -> assertEquals("고블린", monster.name()),
                () -> assertEquals(40, monster.hp()),
                () -> assertEquals(20, monster.attack())
        );
    }

    @Test
    void 몬스터도_잘못된_상태로_생성할_수_없다() {
        assertThatThrownBy(() -> new Monster("고블린", -1, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("체력은 음수일 수 없습니다.");
    }
}
