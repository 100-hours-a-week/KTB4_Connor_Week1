package model;

import org.example.model.Stage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StageTest {

    @Test
    void 스테이지를_생성한다() {
        // given
        int number = 1;

        // when
        Stage stage = new Stage(number);

        // then
        assertEquals(number, stage.value());
    }

    @Test
    void 스테이지는_1이상이어야_한다() {
        // expected
        assertThatThrownBy(() -> new Stage(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("스테이지는 1 이상이어야 합니다.");
    }

    @Test
    void 스테이지는_증가할_수_있다() {
        // given
        Stage stage = new Stage(1);

        // when
        stage.increment();

        // then
        assertEquals(2, stage.value());
    }
}
