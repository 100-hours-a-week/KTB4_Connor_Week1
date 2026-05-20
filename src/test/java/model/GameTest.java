package model;

import org.example.engine.BattleEngine;
import org.example.engine.StageManager;
import org.example.model.Game;
import org.example.model.Warrior;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.model.vo.BattleOption.ATTACK;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void 게임은_1스테이지_몬스터와_함께_시작한다() {
        Game game = game();

        assertAll(
                () -> assertEquals(1, game.stage().value()),
                () -> assertEquals(1, game.monster().stage()),
                () -> assertNotNull(game.player()),
                () -> assertTrue(game.isStageInProgress()),
                () -> assertFalse(game.isStageCleared()),
                () -> assertFalse(game.isOver())
        );
    }

    @Test
    void 턴을_진행하면_전투_결과가_반영된다() {
        Game game = game();

        game.playTurn(ATTACK);

        assertAll(
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
    void 진행_중인_스테이지가_없으면_턴을_진행할_수_없다() {
        Game game = game();
        game.monster().receiveDamage(game.monster().hp());

        assertThrows(IllegalStateException.class, () -> game.playTurn(ATTACK));
    }

    @Test
    void 플레이어가_죽으면_게임이_종료된다() {
        Game game = game();

        game.player().receiveDamage(game.player().hp());

        assertTrue(game.isOver());
    }

    @ParameterizedTest
    @MethodSource("invalidGameStartArguments")
    void 게임_생성에_필요한_값은_null일_수_없다(
            final Warrior warrior,
            final StageManager stageManager,
            final BattleEngine battleEngine
    ) {
        assertThatThrownBy(() -> Game.start(warrior, stageManager, battleEngine))
                .isInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> invalidGameStartArguments() {
        return Stream.of(
                Arguments.of(null, new StageManager(), battleEngine()),
                Arguments.of(new Warrior(), null, battleEngine()),
                Arguments.of(new Warrior(), new StageManager(), null)
        );
    }

    private Game game() {
        return Game.start(new Warrior(), new StageManager(), battleEngine());
    }

    private static BattleEngine battleEngine() {
        return new BattleEngine(new FixedRandom(90));
    }

    private static class FixedRandom extends Random {
        private final int value;

        private FixedRandom(final int value) {
            this.value = value;
        }

        @Override
        public int nextInt(final int bound) {
            return value;
        }
    }
}
