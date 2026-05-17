package org.example.model;

import org.example.model.vo.JobOption;

import java.util.EnumSet;

import static org.example.model.vo.BattleOption.ATTACK;
import static org.example.model.vo.BattleOption.DEFEND;

public class Warrior extends Player {
    private static final int HP = 180;
    private static final int ATTACK_POWER = 25;

    public Warrior() {
        super(JobOption.WARRIOR.label(), HP, ATTACK_POWER, EnumSet.of(ATTACK, DEFEND));
    }
}
