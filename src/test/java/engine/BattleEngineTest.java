package engine;

import org.example.engine.BattleEngine;
import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.example.model.Mage;
import org.example.model.Monster;
import org.example.model.Warrior;
import org.junit.jupiter.api.Test;

import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.DEFEND;
import static org.example.model.vo.BattleOption.SKILL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleEngineTest {
    private final StageManager stageManager = new StageManager();

    @Test
    void 공격하면_몬스터에게_피해를_준다() {
        BattleEngine battleEngine = new BattleEngine(new FixedRandom(10));
        Warrior warrior = new Warrior();
        Monster monster = stageManager.createMonster(1);

        BattleTurnResult result = battleEngine.resolveTurn(warrior, monster, ATTACK);

        assertAll(
                () -> assertEquals(25, result.monsterDamageTaken()),
                () -> assertEquals(20, result.playerDamageTaken()),
                () -> assertTrue(result.monsterAttacked()),
                () -> assertEquals(15, monster.hp()),
                () -> assertEquals(160, warrior.hp()),
                () -> assertFalse(result.monsterDefeated())
        );
    }

    @Test
    void 방어하면_받는_피해가_감소한다() {
        BattleEngine battleEngine = new BattleEngine(new FixedRandom(10));
        Warrior warrior = new Warrior();
        Monster monster = stageManager.createMonster(1);

        BattleTurnResult result = battleEngine.resolveTurn(warrior, monster, DEFEND);

        assertAll(
                () -> assertEquals(0, result.monsterDamageTaken()),
                () -> assertEquals(10, result.playerDamageTaken()),
                () -> assertEquals(170, warrior.hp())
        );
    }

    @Test
    void 스킬은_일반_공격보다_큰_피해를_준다() {
        BattleEngine battleEngine = new BattleEngine(new FixedRandom(10));
        Mage mage = new Mage();
        Monster monster = stageManager.createMonster(6);

        BattleTurnResult result = battleEngine.resolveTurn(mage, monster, SKILL);

        assertAll(
                () -> assertEquals(10, monster.hp()),
                () -> assertEquals(80, result.monsterDamageTaken()),
                () -> assertFalse(result.monsterDefeated())
        );
    }

    @Test
    void 수행할_수_없는_행동은_거부한다() {
        BattleEngine battleEngine = new BattleEngine(new FixedRandom(10));
        Warrior warrior = new Warrior();
        Monster monster = stageManager.createMonster(1);

        assertThrows(IllegalArgumentException.class, () -> battleEngine.resolveTurn(warrior, monster, SKILL));
    }

    @Test
    void 몬스터를_쓰러뜨리면_반격하지_않는다() {
        BattleEngine battleEngine = new BattleEngine(new FixedRandom(10));
        Mage mage = new Mage();
        Monster monster = stageManager.createMonster(1);

        BattleTurnResult result = battleEngine.resolveTurn(mage, monster, SKILL);

        assertAll(
                () -> assertTrue(result.monsterDefeated()),
                () -> assertFalse(result.monsterAttacked()),
                () -> assertEquals(0, result.playerDamageTaken()),
                () -> assertEquals(120, mage.hp())
        );
    }

    @Test
    void 몬스터가_공격하지_않을_수_있다() {
        BattleEngine battleEngine = new BattleEngine(new FixedRandom(90));
        Warrior warrior = new Warrior();
        Monster monster = stageManager.createMonster(1);

        BattleTurnResult result = battleEngine.resolveTurn(warrior, monster, ATTACK);

        assertAll(
                () -> assertFalse(result.monsterAttacked()),
                () -> assertEquals(0, result.playerDamageTaken()),
                () -> assertEquals(180, warrior.hp())
        );
    }

    private static class FixedRandom extends java.util.Random {
        private final int value;

        private FixedRandom(int value) {
            this.value = value;
        }

        @Override
        public int nextInt(int bound) {
            return value;
        }
    }
}
