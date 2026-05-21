package org.example.model;

import org.example.dto.TurnResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.DEFEND;
import static org.example.model.vo.BattleOption.SKILL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void 게임은_1스테이지_몬스터와_함께_시작한다() {
        // given
        Game game = game();

        // expected
        assertAll(
                () -> assertEquals(1, game.stage().value()),
                () -> assertNotNull(game.monster()),
                () -> assertNotNull(game.player()),
                () -> assertFalse(game.isOver())
        );
    }

    @Test
    void 턴을_진행하면_전투_결과가_상태에_반영된다() {
        // given
        Game game = game();

        // when
        TurnResult result = game.playTurn(ATTACK);

        // then
        assertAll(
                () -> assertEquals(ATTACK, result.playerAction()),
                () -> assertEquals(25, result.monsterDamageTaken()),
                () -> assertTrue(Set.of(0, 20).contains(result.playerDamageTaken())),
                () -> assertEquals(result.playerDamageTaken() > 0, result.monsterAttacked()),
                () -> assertFalse(result.monsterDefeated()),
                () -> assertEquals(40 - result.monsterDamageTaken(), game.monster().hp()),
                () -> assertEquals(180 - result.playerDamageTaken(), game.player().hp())
        );
    }

    @Test
    void 방어를_선택하면_몬스터에게_피해를_주지_않는다() {
        // given
        Game game = game();

        // when
        TurnResult result = game.playTurn(DEFEND);

        // then
        assertAll(
                () -> assertEquals(DEFEND, result.playerAction()),
                () -> assertEquals(0, result.monsterDamageTaken()),
                () -> assertEquals(40, game.monster().hp())
        );
    }

    @Test
    void 스킬을_선택하면_공격력의_두_배_피해를_준다() {
        // given
        Game game = Game.start(new Mage(), new NoAttackStageManager());

        // when
        TurnResult result = game.playTurn(SKILL);

        // then
        assertAll(
                () -> assertEquals(SKILL, result.playerAction()),
                () -> assertEquals(80, result.monsterDamageTaken()),
                () -> assertTrue(result.monsterDefeated()),
                () -> assertEquals(0, game.monster().hp())
        );
    }

    @Test
    void 스테이지를_클리어하면_다음_스테이지로_진행한다() {
        // given
        Game game = game();
        game.monster().damage(game.monster().hp());

        // when
        game.nextStage();

        // then
        assertAll(
                () -> assertEquals(2, game.stage().value()),
                () -> assertFalse(game.isOver())
        );
    }

    @Test
    void 진행_중인_스테이지에서는_다음_스테이지로_진행할_수_없다() {
        // expected
        assertThatThrownBy(() -> game().nextStage())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 수행할_수_없는_행동으로는_턴을_진행할_수_없다() {
        // expected
        assertThatThrownBy(() -> game().playTurn(SKILL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("플레이어가 수행할 수 없는 행동입니다.");
    }

    @Test
    void 플레이어가_죽으면_게임이_종료된다() {
        // given
        Game game = game();

        // when
        game.player().damage(game.player().hp());

        // then
        assertTrue(game.isOver());
    }

    @ParameterizedTest
    @MethodSource("provideNullArguments")
    void 게임_생성에_필요한_값은_null일_수_없다(Player player, MonsterFactory stageManager) {
        // expected
        assertThatThrownBy(() -> Game.start(player, stageManager))
                .isInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> provideNullArguments() {
        return Stream.of(
            Arguments.of(null, new GameTest.NoAttackStageManager()),
            Arguments.of(new Warrior(), null)
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
