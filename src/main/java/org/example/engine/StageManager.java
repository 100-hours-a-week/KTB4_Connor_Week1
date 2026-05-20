package org.example.engine;

import org.example.model.Monster;
import org.example.model.Stage;

public class StageManager {
    private static final String MONSTER_NAME = "몬스터";
    private static final int BASE_HP = 40;
    private static final int BASE_ATTACK = 20;
    private static final int HP_INCREMENT = 10;
    private static final int ATTACK_INCREMENT = 5;
    private static final int ATTACK_CHANCE = 70;

    public Monster createMonster(Stage stage) {
        int hp = BASE_HP + (stage.value()) * HP_INCREMENT;
        int attack = BASE_ATTACK + (stage.value() - 1) * ATTACK_INCREMENT;
        return new Monster(MONSTER_NAME + " " + stage, stage.value(), hp, attack, ATTACK_CHANCE);
    }
}
