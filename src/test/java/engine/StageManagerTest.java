package engine;

import org.example.engine.StageManager;
import org.example.model.Monster;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StageManagerTest {
    private final StageManager stageManager = new StageManager();

    @Test
    void 스테이지가_오를수록_몬스터가_강해진다() {
        Monster stageOneMonster = stageManager.createMonster(1);
        Monster stageThreeMonster = stageManager.createMonster(3);

        assertAll(
                () -> assertEquals(40, stageOneMonster.hp()),
                () -> assertEquals(20, stageOneMonster.attack()),
                () -> assertEquals(60, stageThreeMonster.hp()),
                () -> assertEquals(30, stageThreeMonster.attack()),
                () -> assertEquals(3, stageThreeMonster.stage())
        );
    }

    @Test
    void 스테이지는_1_이상이어야_한다() {
        assertThrows(IllegalArgumentException.class, () -> stageManager.createMonster(0));
    }
}
