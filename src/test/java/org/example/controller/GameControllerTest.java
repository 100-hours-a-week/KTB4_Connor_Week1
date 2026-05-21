package org.example.controller;

import org.example.dto.GameMenuOption;
import org.example.model.AttackStrategy;
import org.example.model.MonsterFactory;
import org.example.model.Monster;
import org.example.model.Stage;
import org.example.model.vo.BattleOption;
import org.example.model.vo.JobOption;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static org.example.dto.GameMenuOption.EXIT;
import static org.example.dto.GameMenuOption.PLAY;
import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.SKILL;
import static org.example.model.vo.JobOption.MAGE;
import static org.example.model.vo.JobOption.WARRIOR;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerTest {

    @Test
    void 종료를_선택하면_종료_메시지를_출력한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(EXIT, WARRIOR, ATTACK);
        GameController controller = controllerWith(inputView, output);

        controller.run();

        String message = output.toString();
        assertAll(
                () -> assertTrue(message.contains("게임을 종료합니다.")),
                () -> assertFalse(message.contains("스테이지를 시작합니다.")),
                () -> assertFalse(inputView.jobOptionRequested),
                () -> assertFalse(inputView.battleOptionRequested)
        );
    }

    @Test
    void 전사는_가능한_행동만_입력_뷰에_전달한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(PLAY, WARRIOR, ATTACK);
        GameController controller = controllerWith(inputView, output);

        controller.run();

        String message = output.toString();
        assertAll(
                () -> assertEquals(Set.of(ATTACK, BattleOption.DEFEND), inputView.lastBattleOptions),
                () -> assertTrue(message.contains("Warrior (전사)의 공격 -")),
                () -> assertTrue(message.contains("플레이어가 패배했습니다. 게임 오버!"))
        );
    }

    @Test
    void 마법사는_방어를_제외한_행동만_입력_뷰에_전달한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(PLAY, MAGE, SKILL);
        GameController controller = controllerWith(inputView, output);

        controller.run();

        String message = output.toString();
        assertAll(
                () -> assertEquals(Set.of(ATTACK, SKILL), inputView.lastBattleOptions),
                () -> assertTrue(message.contains("스테이지를 시작합니다.")),
                () -> assertTrue(message.contains("Mage (메이지)의 스킬 -")),
                () -> assertTrue(message.contains("플레이어가 패배했습니다. 게임 오버!"))
        );
    }

    @Test
    void 몬스터를_처치하면_다음_스테이지로_진행한다() {
        // given
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(PLAY, WARRIOR, ATTACK);
        GameController controller = new GameController(
                inputView,
                new OutputView(new PrintStream(output)),
                new StageClearThenPlayerDefeatingStageManager()
        );

        // when
        controller.run();

        // then
        String message = output.toString();
        assertAll(
                () -> assertTrue(message.contains("1 스테이지를 시작합니다.")),
                () -> assertTrue(message.contains("스테이지 클리어! 처치한 몬스터: 1스테이지 몬스터")),
                () -> assertTrue(message.contains("2 스테이지를 시작합니다.")),
                () -> assertTrue(message.contains("플레이어가 패배했습니다. 게임 오버!")),
                () -> assertEquals(2, inputView.battleOptionRequestCount)
        );
    }

    private GameController controllerWith(FakeInputView inputView,
                                          ByteArrayOutputStream output) {
        return new GameController(
                inputView,
                new OutputView(new PrintStream(output)),
                new PlayerDefeatingStageManager()
        );
    }

    private static class FakeInputView extends InputView {
        private final GameMenuOption menuOption;
        private final JobOption jobOption;
        private final BattleOption battleOption;
        private Set<BattleOption> lastBattleOptions;
        private boolean jobOptionRequested;
        private boolean battleOptionRequested;
        private int battleOptionRequestCount;

        private FakeInputView(final GameMenuOption menuOption,
                              final JobOption jobOption,
                              final BattleOption battleOption) {
            this.menuOption = menuOption;
            this.jobOption = jobOption;
            this.battleOption = battleOption;
        }

        @Override
        public GameMenuOption inputMenuOption() {
            return menuOption;
        }

        @Override
        public JobOption inputJobOption() {
            jobOptionRequested = true;
            return jobOption;
        }

        @Override
        public BattleOption inputBattleOption(final Set<BattleOption> options) {
            battleOptionRequested = true;
            battleOptionRequestCount++;
            if (battleOptionRequestCount > 2) {
                throw new AssertionError("전투 행동을 예상보다 많이 요청했습니다.");
            }
            lastBattleOptions = options;
            return battleOption;
        }
    }

    private static class PlayerDefeatingStageManager extends MonsterFactory {
        @Override
        public Monster create(final Stage stage) {
            return new AlwaysAttackMonster("테스트 몬스터", 1_000, 1_000);
        }
    }

    private static class StageClearThenPlayerDefeatingStageManager extends MonsterFactory {
        @Override
        public Monster create(final Stage stage) {
            if (stage.value() == 1) {
                return Monster.builder()
                        .name("1스테이지 몬스터")
                        .hp(1)
                        .attack(0)
                        .build();
            }

            return new AlwaysAttackMonster("2스테이지 몬스터", 1_000, 1_000);
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
