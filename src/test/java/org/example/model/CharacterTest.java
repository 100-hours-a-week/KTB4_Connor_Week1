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
        // given
        Character character = Character.builder()
                .name("example")
                .hp(30)
                .attack(10)
                .build();

        // when
        character.damage(12);

        // then
        assertAll(
                () -> assertEquals(18, character.hp()),
                () -> assertTrue(character.isAlive())
        );
    }

    @Test
    void 체력은_0보다_작아지지_않는다() {
        // given
        Character character = Character.builder()
                .name("example")
                .hp(10)
                .attack(10)
                .build();

        // when
        character.damage(20);

        // then
        assertAll(
                () -> assertEquals(0, character.hp()),
                () -> assertFalse(character.isAlive())
        );
    }

    @Test
    void 공격_전략이_성공하면_공격력을_반환한다() {
        // given
        Character character = Character.builder()
                .name("example")
                .hp(30)
                .attack(10)
                .build();

        // when
        AttackStrategy strategy = () -> true;

        // then
        assertEquals(10, character.attack(strategy));
    }

    @Test
    void 공격_전략이_실패하면_0을_반환한다() {
        //given
        Character character = Character.builder()
                .name("example")
                .hp(30)
                .attack(10)
                .build();

        // when
        AttackStrategy strategy = () -> false;

        assertEquals(0, character.attack(strategy));
    }

    @Test
    void 이름은_비어_있을_수_없다() {
        // expected
        assertThatThrownBy(() -> new Character("", 10, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 체력과_공격력은_음수일_수_없다() {
        // expected
        assertAll(
                () -> assertThatThrownBy(() -> new Character("example", -1, 10))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new Character("example", 10, -1))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
