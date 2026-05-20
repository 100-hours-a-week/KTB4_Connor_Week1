package org.example.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test
    void 캐릭터를_생성한다() {
        // given
        String name = "example";
        int hp = 0;
        int attack = 0;

        // when
        Character character = Character.builder()
                .name(name)
                .hp(hp)
                .attack(attack)
                .build();

        // then
        assertAll(
                () -> assertEquals(name, character.name()),
                () -> assertEquals(hp, character.hp()),
                () -> assertEquals(attack, character.attack())
        );
    }

    @Test
    void 피해를_받으면_체력이_감소한다() {
        Character character = Character.builder()
                .name("example")
                .hp(30)
                .attack(10)
                .build();

        character.damage(12);

        assertAll(
                () -> assertEquals(18, character.hp()),
                () -> assertTrue(character.isAlive())
        );
    }

    @Test
    void 체력은_0보다_작아지지_않는다() {
        Character character = Character.builder()
                .name("example")
                .hp(10)
                .attack(10)
                .build();

        character.damage(20);

        assertAll(
                () -> assertEquals(0, character.hp()),
                () -> assertFalse(character.isAlive())
        );
    }

    @Test
    void 공격_전략이_성공하면_공격력을_반환한다() {
        Character character = Character.builder()
                .name("example")
                .hp(30)
                .attack(10)
                .build();

        assertEquals(10, character.attack(() -> true));
    }

    @Test
    void 공격_전략이_실패하면_0을_반환한다() {
        Character character = Character.builder()
                .name("example")
                .hp(30)
                .attack(10)
                .build();

        assertEquals(0, character.attack(() -> false));
    }

    @Test
    void 이름은_비어_있을_수_없다() {
        assertThatThrownBy(() -> Character.builder().name("").hp(10).attack(10).build())
                .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 체력과_공격력은_음수일_수_없다() {
        assertAll(
                () -> assertThatThrownBy(() -> Character.builder().name("example").hp(-1).attack(10).build()),
                () -> assertThatThrownBy(() -> Character.builder().name("example").hp(10).attack(-1).build())
        );
    }
}
