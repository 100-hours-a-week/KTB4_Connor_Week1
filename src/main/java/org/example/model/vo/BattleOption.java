package org.example.model.vo;

public enum BattleOption {
    ATTACK(1, "공격"),
    DEFEND(2, "방어"),
    SKILL(3, "스킬");

    private final int number;
    private final String label;

    BattleOption(int number, String label) {
        this.number = number;
        this.label = label;
    }

    public int number() {
        return number;
    }

    public String label() {
        return label;
    }
}
