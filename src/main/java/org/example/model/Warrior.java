package org.example.model;

import org.example.model.vo.JobOption;

public class Warrior extends Player {
    private static final int HP = 180;
    private static final int ATTACK = 25;

    public Warrior() {
        super(JobOption.WARRIOR.label(), HP, ATTACK);
    }
}
