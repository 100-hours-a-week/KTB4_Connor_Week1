package org.example.model;

import org.example.model.vo.JobOption;

public class Monster extends Character {
    private static final String NAME = "몬스터";
    private static final int BASE_HP = 40;
    private static final int BASE_ATTACK = 20;
    private final int stage;

    public Monster(int stage) {
        super(NAME + " " + stage, BASE_HP + (stage - 1) * 10, BASE_ATTACK + (stage - 1) * 5);
        this.stage = stage;
    }
}
