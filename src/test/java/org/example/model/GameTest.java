package org.example.model;

import org.example.dto.TurnResult;
import org.junit.jupiter.api.Test;

import java.util.Set;

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
                () -> assertNotNull(game.monster()),
                () -> assertNotNull(game.player()),
                () -> assertFalse(game.isOver())
        );
    }

    @Test
    void 턴을_진행하면_전투_결과가_상태에_반영된다() {
        Game game = game();

        TurnResult result = game.playTurn(ATTACK);

        assertAll(
                () -> assertEquals(ATTACK, result.playerAction()),
                () -> assertTrue(Set.of(0, 25).contains(result.monsterDamageTaken())),
                () -> assertTrue(Set.of(0, 20).contains(result.playerDamageTaken())),
                () -> assertEquals(result.playerDamageTaken() > 0, result.monsterAttacked()),
                () -> assertFalse(result.monsterDefeated()),
                () -> assertEquals(40 - result.monsterDamageTaken(), game.monster().hp()),
                () -> assertEquals(180 - result.playerDamageTaken(), game.player().hp())
        );
    }

    @Test
    void 스테이지를_클리어하면_다음_스테이지로_진행한다() {
        Game game = game();
        game.monster().damage(game.monster().hp());

        game.nextStage();

        assertAll(
                () -> assertEquals(2, game.stage().value()),
                () -> assertFalse(game.isOver())
        );
    }

    @Test
    void 진행_중인_스테이지에서는_다음_스테이지로_진행할_수_없다() {
        assertThrows(IllegalStateException.class, () -> game().nextStage());
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

        game.player().damage(game.player().hp());

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

    private static class NoAttackStageManager extends MonsterFactory {
        @Override
        public Monster create(final Stage stage) {
            return Monster.builder()
                    .name("테스트 몬스터")
                    .hp(40)
                    .attack(20)
                    .build();
        }
    }
}
