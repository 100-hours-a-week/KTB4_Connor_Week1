package org.example.util;

import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class Randoms {

    private static final Random defaultRandom = ThreadLocalRandom.current();

    public static int pickNumberInRange(final int startInclusive, final int endInclusive) {
        validateRange(startInclusive, endInclusive);
        return startInclusive + defaultRandom.nextInt(endInclusive - startInclusive + 1);
    }

    private static void validateRange(final int startInclusive, final int endInclusive) {
        if (startInclusive > endInclusive) {
            throw new IllegalArgumentException("시작 값은 종료 값보다 클 수 없습니다.");
        }
        if (endInclusive == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("종료 값이 정수형의 최대값 보다 클 수 없습니다.");
        }
    }
}
