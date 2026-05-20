package org.example.model;

public class MonsterFactory {
    private static final String MONSTER_NAME = "Lv.%d 고블린";
    private static final int BASE_HP = 40;
    private static final int BASE_ATTACK = 20;
    private static final int HP_INCREMENT = 10;
    private static final int ATTACK_INCREMENT = 5;

    public Monster create(Stage stage) {
        int hp = BASE_HP + (stage.value() - 1) * HP_INCREMENT;
        int attack = BASE_ATTACK + (stage.value() - 1) * ATTACK_INCREMENT;
        return Monster.builder()
                .name(MONSTER_NAME.formatted(stage.value()))
                .hp(hp)
                .attack(attack)
                .build();
    }
}
