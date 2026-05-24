package org.example.model;

import org.junit.jupiter.api.Test;

import static org.example.model.vo.JobOption.MAGE;
import static org.example.model.vo.JobOption.WARRIOR;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PlayerFactoryTest {
    private final PlayerFactory playerFactory = new PlayerFactory();

    @Test
    void 직업_옵션에_따라_플레이어를_생성한다() {
        assertAll(
                () -> assertInstanceOf(Warrior.class, playerFactory.create(WARRIOR)),
                () -> assertInstanceOf(Mage.class, playerFactory.create(MAGE))
        );
    }
}
