package model;

import org.example.model.Monster;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MonsterTest {

    @Test
    void 몬스터의_스테이지는_1_이상이어야_한다() {
        assertThrows(IllegalArgumentException.class, () -> new Monster("몬스터", 0, 10, 10, 50));
    }

    @Test
    void 몬스터의_공격_확률은_0부터_100_사이여야_한다() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new Monster("몬스터", 1, 10, 10, -1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Monster("몬스터", 1, 10, 10, 101))
        );
    }
}
