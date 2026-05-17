package org.example.engine;

import org.example.model.Monster;

public class StageManager {
    private static final String MONSTER_NAME = "몬스터";
    private static final int BASE_HP = 40;
    private static final int BASE_ATTACK = 20;
    private static final int HP_INCREMENT = 10;
    private static final int ATTACK_INCREMENT = 5;
    private static final int ATTACK_CHANCE = 70;

    public Monster createMonster(int stage) {
        if (stage < 1) {
            throw new IllegalArgumentException("스테이지는 1 이상이어야 합니다.");
        }

        int hp = BASE_HP + (stage - 1) * HP_INCREMENT;
        int attack = BASE_ATTACK + (stage - 1) * ATTACK_INCREMENT;
        return new Monster(MONSTER_NAME + " " + stage, stage, hp, attack, ATTACK_CHANCE);
    }
}
