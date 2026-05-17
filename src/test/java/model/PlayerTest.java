package model;

import org.example.model.Player;
import org.junit.jupiter.api.Test;

import static org.example.model.Player.builder;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    @Test
    void 플레이어를_생성한다() {
        // given
        String name = "example";
        int hp = 0;
        int attack = 0;

        // when
        Player player = builder()
                .name(name)
                .build();

        // then
        assertAll(
                () -> assertEquals(name, player.name()),
                () -> assertEquals(hp, player.hp()),
                () -> assertEquals(attack, player.attack())
        );
    }
}