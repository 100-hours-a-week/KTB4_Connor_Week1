package org.example.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomsTest {

    @Test
    void 범위_안의_숫자를_생성한다() {
        for (int i = 0; i < 100; i++) {
            int number = Randoms.pickNumberInRange(0, 9);

            assertTrue(number >= 0 && number <= 9);
        }
    }

    @Test
    void 시작값이_끝값보다_크면_예외가_발생한다() {
        assertThrows(IllegalArgumentException.class,
                () -> Randoms.pickNumberInRange(9, 0));
    }

    @Test
    void 끝값은_Integer_MAX_VALUE일_수_없다() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Randoms.pickNumberInRange(0, Integer.MAX_VALUE)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Randoms.pickNumberInRange(Integer.MAX_VALUE, Integer.MAX_VALUE))
        );
    }
}
