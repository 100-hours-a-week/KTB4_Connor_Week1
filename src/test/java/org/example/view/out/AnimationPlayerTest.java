package org.example.view.out;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimationPlayerTest {

    @Test
    void 애니메이션을_출력한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnimationPlayer animationPlayer = new AnimationPlayer(new PrintStream(output), true, 0L);

        animationPlayer.play("default");

        String message = output.toString();
        assertAll(
                () -> assertTrue(message.contains("\033[?25l")),
                () -> assertTrue(message.contains("\033[?25h")),
                () -> assertTrue(message.contains("\033[2J"))
        );
    }

    @Test
    void 애니메이션이_비활성화되면_출력하지_않는다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnimationPlayer animationPlayer = new AnimationPlayer(new PrintStream(output), false, 100L);

        animationPlayer.play("default");

        assertTrue(output.toString().isEmpty());
    }
}
