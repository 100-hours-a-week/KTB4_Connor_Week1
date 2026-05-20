package org.example.engine;

import org.example.model.Monster;
import org.example.model.MonsterFactory;
import org.example.model.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MonsterFactoryTest {
    private final MonsterFactory monsterFactory = new MonsterFactory();

    @Test
    void 스테이지가_오를수록_몬스터가_강해진다() {
        Monster stageOneMonster = monsterFactory.create(new Stage(1));
        Monster stageThreeMonster = monsterFactory.create(new Stage(3));

        assertAll(
                () -> assertEquals("Lv.1 고블린", stageOneMonster.name()),
                () -> assertEquals(40, stageOneMonster.hp()),
                () -> assertEquals(20, stageOneMonster.attack()),
                () -> assertEquals("Lv.3 고블린", stageThreeMonster.name()),
                () -> assertEquals(60, stageThreeMonster.hp()),
                () -> assertEquals(30, stageThreeMonster.attack())
        );
    }
}
