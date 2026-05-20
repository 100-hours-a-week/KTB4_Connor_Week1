package org.example.model;

import lombok.NoArgsConstructor;
import org.example.util.Randoms;

@NoArgsConstructor
public class RandomAttackStrategy implements AttackStrategy {

    private static final int ATTACKABLE_NUMBER = 4;
    private static final int RANDOM_MAX_RANGE = 9;
    private static final int RANDOM_MIN_RANGE = 0;

    @Override
    public boolean attackable() {
        int randomNumber = getRandomNumber();
        return ATTACKABLE_NUMBER <= randomNumber;
    }

    private int getRandomNumber() {
        return Randoms.pickNumberInRange(RANDOM_MIN_RANGE, RANDOM_MAX_RANGE);
    }
}
