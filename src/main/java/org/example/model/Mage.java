package org.example.model;

import org.example.model.vo.JobOption;


public class Mage extends Player {
    private static final int HP = 120;
    private static final int ATTACK = 40;

    public Mage() {
        super(JobOption.MAGE.label(), HP, ATTACK);
    }
}
