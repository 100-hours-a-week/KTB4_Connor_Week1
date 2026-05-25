package org.example.model;

import org.example.dto.MonsterAttackResult;
import org.example.dto.TurnResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
        TurnResult result = game.playTurn(ATTACK).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(ATTACK, result.playerAction()),
                () -> assertEquals(25, result.monsterDamageTaken()),
                () -> assertFalse(result.monsterDefeated()),
                () -> assertEquals(40 - result.monsterDamageTaken(), game.monster().hp()),
                () -> assertEquals(180, game.player().hp())
        );
    }

    @Test
    void 몬스터_자동_공격은_플레이어_체력을_감소시킨다() {
        // given
        Game game = Game.start(new Warrior(), new AlwaysAttackStageManager());

        // when
        MonsterAttackResult result = game.automaticAttack().orElseThrow();

        // then
        assertAll(
                () -> assertEquals(20, result.playerDamageTaken()),
                () -> assertEquals(160, result.playerHp()),
                () -> assertEquals(160, game.player().hp())
        );
    }

    @Test
    void 종료된_전투에서는_더_이상_공격하지_않는다() {
        // given
        Game defeatedPlayerGame = Game.start(new Warrior(), new AlwaysAttackStageManager());
        defeatedPlayerGame.player().damage(defeatedPlayerGame.player().hp());
        Game clearedGame = game();
        clearedGame.monster().damage(clearedGame.monster().hp());

        // expected
        assertAll(
                () -> assertTrue(defeatedPlayerGame.playTurn(SKILL).isEmpty()),
                () -> assertTrue(defeatedPlayerGame.automaticAttack().isEmpty()),
                () -> assertTrue(clearedGame.automaticAttack().isEmpty())
        );
    }

    @Test
    void 방어를_선택하면_몬스터에게_피해를_주지_않는다() {
        // given
        Game game = game();

        // when
        TurnResult result = game.playTurn(DEFEND).orElseThrow();

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
        TurnResult result = game.playTurn(SKILL).orElseThrow();

        // then
        assertAll(
                () -> assertEquals(SKILL, result.playerAction()),
                () -> assertEquals(80, result.monsterDamageTaken()),
                () -> assertTrue(result.monsterDefeated()),
                () -> assertEquals(0, game.monster().hp()),
                () -> assertTrue(game.isClear())
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
        // given
        Game game = game();

        // expected
        assertThatThrownBy(game::nextStage)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 수행할_수_없는_행동으로는_턴을_진행할_수_없다() {
        // given
        Game game = game();

        // expected
        assertThatThrownBy(() -> game.playTurn(SKILL))
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
            return new Monster("테스트 몬스터", 40, 20);
        }
    }

    private static class AlwaysAttackStageManager extends MonsterFactory {
        @Override
        public Monster create(final Stage stage) {
            return new AlwaysAttackMonster("테스트 몬스터", 40, 20);
        }
    }

    private static class AlwaysAttackMonster extends Monster {
        private AlwaysAttackMonster(final String name,
                                    final int hp,
                                    final int attack) {
            super(name, hp, attack);
        }

        @Override
        public int attack(final AttackStrategy strategy) {
            return attack();
        }
    }
}
