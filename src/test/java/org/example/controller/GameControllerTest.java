package org.example.controller;

import org.example.dto.GameMenuOption;
import org.example.engine.BattleEngine;
import org.example.engine.BattleTurnResult;
import org.example.model.Monster;
import org.example.model.Player;
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
        GameController controller = controllerWith(inputView, output, new PlayerDefeatingBattleEngine());

        controller.run();

        String message = output.toString();
        assertAll(
                () -> assertTrue(message.contains("게임을 종료합니다.")),
                () -> assertFalse(message.contains("스테이지를 시작합니다."))
        );
    }

    @Test
    void 전사는_가능한_행동만_입력_뷰에_전달한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(PLAY, WARRIOR, ATTACK);
        PlayerDefeatingBattleEngine battleEngine = new PlayerDefeatingBattleEngine();
        GameController controller = controllerWith(inputView, output, battleEngine);

        controller.run();

        assertAll(
                () -> assertEquals(Set.of(ATTACK, BattleOption.DEFEND), inputView.lastBattleOptions),
                () -> assertEquals(ATTACK, battleEngine.lastPlayerAction)
        );
    }

    @Test
    void 마법사는_방어를_제외한_행동만_입력_뷰에_전달한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        FakeInputView inputView = new FakeInputView(PLAY, MAGE, SKILL);
        PlayerDefeatingBattleEngine battleEngine = new PlayerDefeatingBattleEngine();
        GameController controller = controllerWith(inputView, output, battleEngine);

        controller.run();

        String message = output.toString();
        assertAll(
                () -> assertEquals(Set.of(ATTACK, SKILL), inputView.lastBattleOptions),
                () -> assertEquals(SKILL, battleEngine.lastPlayerAction),
                () -> assertTrue(message.contains("1 스테이지를 시작합니다.")),
                () -> assertTrue(message.contains("플레이어가 패배했습니다. 게임 오버!"))
        );
    }

    private GameController controllerWith(FakeInputView inputView,
                                          ByteArrayOutputStream output,
                                          BattleEngine battleEngine) {
        return new GameController(
                inputView,
                new OutputView(new PrintStream(output)),
                battleEngine,
                new org.example.engine.StageManager()
        );
    }

    private static class FakeInputView extends InputView {
        private final GameMenuOption menuOption;
        private final JobOption jobOption;
        private final BattleOption battleOption;
        private Set<BattleOption> lastBattleOptions;

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
            return jobOption;
        }

        @Override
        public BattleOption inputBattleOption(final Set<BattleOption> options) {
            lastBattleOptions = options;
            return battleOption;
        }
    }

    private static class PlayerDefeatingBattleEngine extends BattleEngine {
        private BattleOption lastPlayerAction;

        @Override
        public BattleTurnResult resolveTurn(final Player player,
                                            final Monster monster,
                                            final BattleOption playerAction) {
            lastPlayerAction = playerAction;
            final int playerDamageTaken = player.receiveDamage(player.hp());

            return new BattleTurnResult(
                    playerAction,
                    0,
                    playerDamageTaken,
                    true,
                    false,
                    true
            );
        }
    }
}
