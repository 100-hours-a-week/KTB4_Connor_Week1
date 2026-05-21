package org.example.view.out;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimationPlayerTest {

    @Test
    void 애니메이션을_별도_스레드에서_출력한다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnimationPlayer animationPlayer = new AnimationPlayer(new PrintStream(output), true, 100L);

        try {
            CompletableFuture<Void> future = animationPlayer.play("default");

            assertFalse(future.isDone());

            future.join();

            String message = output.toString();
            assertAll(
                    () -> assertTrue(message.contains("\033[?25l")),
                    () -> assertTrue(message.contains("\033[?25h")),
                    () -> assertTrue(message.contains("\033[2J"))
            );
        } finally {
            animationPlayer.shutdown();
        }
    }

    @Test
    void 애니메이션이_비활성화되면_즉시_완료된다() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnimationPlayer animationPlayer = new AnimationPlayer(new PrintStream(output), false, 100L);

        try {
            CompletableFuture<Void> future = animationPlayer.play("default");

            assertAll(
                    () -> assertTrue(future.isDone()),
                    () -> assertTrue(output.toString().isEmpty())
            );
        } finally {
            animationPlayer.shutdown();
        }
    }
}
