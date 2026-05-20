package org.example.engine;

import org.example.model.Monster;
import org.example.model.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StageManagerTest {
    private final StageManager stageManager = new StageManager();

    @Test
    void 스테이지가_오를수록_몬스터가_강해진다() {
        Monster stageOneMonster = stageManager.createMonster(new Stage(1));
        Monster stageThreeMonster = stageManager.createMonster(new Stage(3));

        assertAll(
                () -> assertEquals(40, stageOneMonster.hp()),
                () -> assertEquals(20, stageOneMonster.attack()),
                () -> assertEquals(70, stageOneMonster.attackChance()),
                () -> assertEquals(60, stageThreeMonster.hp()),
                () -> assertEquals(30, stageThreeMonster.attack()),
                () -> assertEquals(3, stageThreeMonster.stage())
        );
    }
}
