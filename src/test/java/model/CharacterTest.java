package model;

import org.example.model.Character;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
