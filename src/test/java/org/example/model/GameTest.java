package org.example.model;

import org.example.engine.BattleTurnResult;
import org.example.engine.StageManager;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.SKILL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void 게임은_1스테이지_몬스터와_함께_시작한다() {
        Game game = game();

        assertAll(
                () -> assertEquals(1, game.stage().value()),
                () -> assertEquals(1, game.monster().stage()),
                () -> assertNotNull(game.player()),
                () -> assertTrue(game.isStageInProgress()),
                () -> assertFalse(game.isOver())
        );
    }

    @Test
    void 턴을_진행하면_전투_결과가_상태에_반영된다() {
        Game game = game();

        BattleTurnResult result = game.playTurn(ATTACK);

        assertAll(
                () -> assertEquals(25, result.monsterDamageTaken()),
                () -> assertFalse(result.monsterAttacked()),
                () -> assertEquals(15, game.monster().hp()),
                () -> assertEquals(180, game.player().hp())
        );
    }

    @Test
    void 스테이지를_클리어하면_다음_스테이지로_진행한다() {
        Game game = game();
        game.monster().receiveDamage(game.monster().hp());

        game.proceedNextStage();

        assertAll(
                () -> assertEquals(2, game.stage().value()),
                () -> assertEquals(2, game.monster().stage()),
                () -> assertTrue(game.isStageInProgress())
        );
    }

    @Test
    void 진행_중인_스테이지에서는_다음_스테이지로_진행할_수_없다() {
        assertThrows(IllegalStateException.class, () -> game().proceedNextStage());
    }

    @Test
    void 진행_중인_스테이지가_없으면_턴을_진행할_수_없다() {
        Game game = game();
        game.monster().receiveDamage(game.monster().hp());

        assertThrows(IllegalStateException.class, () -> game.playTurn(ATTACK));
    }

    @Test
    void 수행할_수_없는_행동으로는_턴을_진행할_수_없다() {
        assertThatThrownBy(() -> game().playTurn(SKILL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("플레이어가 수행할 수 없는 행동입니다.");
    }

    @Test
    void 플레이어가_죽으면_게임이_종료된다() {
        Game game = game();

        game.player().receiveDamage(game.player().hp());

        assertTrue(game.isOver());
    }

    @Test
    void 게임_생성에_필요한_값은_null일_수_없다() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> Game.start(null, new NoAttackStageManager())),
                () -> assertThrows(NullPointerException.class,
                        () -> Game.start(new Warrior(), null))
        );
    }

    private Game game() {
        return Game.start(new Warrior(), new NoAttackStageManager());
    }

    private static class NoAttackStageManager extends StageManager {
        @Override
        public Monster createMonster(final Stage stage) {
            return new Monster("테스트 몬스터 " + stage.value(), stage.value(), 40, 20, 0);
        }
    }
}
