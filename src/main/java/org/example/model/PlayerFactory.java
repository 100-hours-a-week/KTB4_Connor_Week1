package org.example.model;

import org.example.model.vo.JobOption;

public class PlayerFactory {

    public Player create(final JobOption jobOption) {
        return switch (jobOption) {
            case WARRIOR -> new Warrior();
            case MAGE -> new Mage();
        };
    }
}
