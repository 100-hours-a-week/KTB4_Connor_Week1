package org.example.model.vo;

public enum BattleOption implements Option {
    ATTACK(1, "공격"),
    DEFEND(2, "방어"),
    SKILL(3, "스킬");

    private final int number;
    private final String label;

    BattleOption(int number, String label) {
        this.number = number;
        this.label = label;
    }

    @Override
    public int number() {
        return number;
    }

    @Override
    public String label() {
        return label;
    }
}
