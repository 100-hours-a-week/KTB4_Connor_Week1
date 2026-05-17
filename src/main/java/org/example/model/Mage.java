package org.example.model;

import org.example.model.vo.JobOption;

import java.util.EnumSet;

import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.SKILL;

public class Mage extends Player {
    private static final int HP = 120;
    private static final int ATTACK_POWER = 40;

    public Mage() {
        super(JobOption.MAGE.label(), HP, ATTACK_POWER, EnumSet.of(ATTACK, SKILL));
    }
}
