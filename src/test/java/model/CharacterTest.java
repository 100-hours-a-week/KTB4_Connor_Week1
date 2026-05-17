package model;

import org.example.model.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        int damage = character.receiveDamage(12);

        assertAll(
                () -> assertEquals(12, damage),
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

        int damage = character.receiveDamage(20);

        assertAll(
                () -> assertEquals(10, damage),
                () -> assertEquals(0, character.hp()),
                () -> assertFalse(character.isAlive())
        );
    }
}
