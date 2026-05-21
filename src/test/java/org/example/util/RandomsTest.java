package org.example.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomsTest {

    @Test
    void 범위_안의_숫자를_생성한다() {
        for (int i = 0; i < 100; i++) {
            // given
            int number = Randoms.pickNumberInRange(0, 9);

            // expected
            assertTrue(number >= 0 && number <= 9);
        }
    }

    @Test
    void 시작값이_끝값보다_크면_예외가_발생한다() {
        // expected
        assertThatThrownBy(() -> Randoms.pickNumberInRange(9, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 끝값은_Integer_MAX_VALUE일_수_없다() {
        // expected
        assertAll(
                () -> assertThatThrownBy(() -> Randoms.pickNumberInRange(0, Integer.MAX_VALUE))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> Randoms.pickNumberInRange(Integer.MAX_VALUE, Integer.MAX_VALUE))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
