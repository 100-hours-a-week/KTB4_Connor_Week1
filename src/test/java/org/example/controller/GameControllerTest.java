package org.example.controller;

import org.example.dto.GameMenuOption;
import org.example.dto.MonsterAttackResult;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    private static final long TEST_ATTACK_INTERVAL_MILLIS = 1L;

    @Test
    void 종료를_선택하면_종료_메시지를_출력한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(EXIT, WARRIOR, ATTACK);
        GameController controller = controllerWith(inputView, new OutputView(new PrintStream(output)));

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
        AutomaticAttackOutputView outputView = new AutomaticAttackOutputView(new PrintStream(output));
        FakeInputView inputView = new FakeInputView(PLAY, WARRIOR, ATTACK, 1, outputView.playerDefeated);
        GameController controller = controllerWith(inputView, outputView);

        controller.run();

        String message = output.toString();
        assertAll(
                () -> assertEquals(Set.of(ATTACK, BattleOption.DEFEND), inputView.lastBattleOptions),
                () -> assertTrue(message.contains("[몬스터 공격]")),
                () -> assertEquals("monster-attack", outputView.attackThreadName),
                () -> assertFalse(message.contains("Warrior (전사)의 공격 -")),
                () -> assertTrue(message.contains("플레이어가 패배했습니다. 게임 오버!"))
        );
    }

    @Test
    void 마법사는_방어를_제외한_행동만_입력_뷰에_전달한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AutomaticAttackOutputView outputView = new AutomaticAttackOutputView(new PrintStream(output));
        FakeInputView inputView = new FakeInputView(PLAY, MAGE, SKILL, 2, outputView.playerDefeated);
        GameController controller = new GameController(
                inputView,
                outputView,
                new StageClearThenPlayerDefeatingStageManager(),
                TEST_ATTACK_INTERVAL_MILLIS
        );

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
        AutomaticAttackOutputView outputView = new AutomaticAttackOutputView(new PrintStream(output));
        FakeInputView inputView = new FakeInputView(PLAY, WARRIOR, ATTACK, 2, outputView.playerDefeated);
        GameController controller = new GameController(
                inputView,
                outputView,
                new StageClearThenPlayerDefeatingStageManager(),
                TEST_ATTACK_INTERVAL_MILLIS
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
                                          OutputView outputView) {
        return new GameController(
                inputView,
                outputView,
                new PlayerDefeatingStageManager(),
                TEST_ATTACK_INTERVAL_MILLIS
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
        private final int requestToWaitForDefeat;
        private final CountDownLatch playerDefeated;

        private FakeInputView(final GameMenuOption menuOption,
                              final JobOption jobOption,
                              final BattleOption battleOption) {
            this(menuOption, jobOption, battleOption, Integer.MAX_VALUE, null);
        }

        private FakeInputView(final GameMenuOption menuOption,
                              final JobOption jobOption,
                              final BattleOption battleOption,
                              final int requestToWaitForDefeat,
                              final CountDownLatch playerDefeated) {
            this.menuOption = menuOption;
            this.jobOption = jobOption;
            this.battleOption = battleOption;
            this.requestToWaitForDefeat = requestToWaitForDefeat;
            this.playerDefeated = playerDefeated;
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
            if (battleOptionRequestCount == requestToWaitForDefeat) {
                awaitPlayerDefeat();
            }
            return battleOption;
        }

        private void awaitPlayerDefeat() {
            try {
                if (!playerDefeated.await(1, TimeUnit.SECONDS)) {
                    throw new AssertionError("몬스터 자동 공격으로 플레이어가 쓰러지지 않았습니다.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError("플레이어 사망 대기 중 인터럽트되었습니다.", e);
            }
        }
    }

    private static class AutomaticAttackOutputView extends OutputView {
        private final CountDownLatch playerDefeated = new CountDownLatch(1);
        private volatile String attackThreadName;

        private AutomaticAttackOutputView(final PrintStream printStream) {
            super(printStream);
        }

        @Override
        public void printMonsterAttackResult(final MonsterAttackResult result) {
            super.printMonsterAttackResult(result);
            attackThreadName = Thread.currentThread().getName();
            if (result.playerDefeated()) {
                playerDefeated.countDown();
            }
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
                return new Monster("1스테이지 몬스터", 1, 0);
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
