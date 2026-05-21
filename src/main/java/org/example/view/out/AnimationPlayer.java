package org.example.view.out;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnimationPlayer {
    private static final String HIDE_CURSOR = "\033[?25l";
    private static final String SHOW_CURSOR = "\033[?25h";
    private static final String CLEAR_SCREEN = "\033[2J";
    private static final String MOVE_CURSOR_HOME = "\033[H";
    private static final Path STATIC_RESOURCE_PATH = Path.of("src", "main", "resources", "static");
    private static final long DEFAULT_ANIMATION_DELAY_MILLIS = 400L;

    private final PrintStream printStream;
    private final ExecutorService executorService;
    private final boolean animationEnabled;
    private final long delayMillis;

    public AnimationPlayer(final PrintStream printStream,
                           final boolean animationEnabled) {
        this(printStream, animationEnabled, DEFAULT_ANIMATION_DELAY_MILLIS);
    }

    AnimationPlayer(final PrintStream printStream,
                    final boolean animationEnabled,
                    final long delayMillis) {
        this.printStream = Objects.requireNonNull(printStream);
        this.animationEnabled = animationEnabled;
        this.delayMillis = delayMillis;
        this.executorService = Executors.newSingleThreadExecutor(runnable -> {
            Thread thread = new Thread(runnable, "animation-player");
            thread.setDaemon(true);
            return thread;
        });
    }

    public CompletableFuture<Void> play(final String action) {
        if (!animationEnabled || executorService.isShutdown()) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.runAsync(() -> printAnimation(action), executorService);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private void printAnimation(final String action) {
        boolean cursorHidden = false;
        try {
            String frame = readFrame(action);
            printStream.print(HIDE_CURSOR);
            cursorHidden = true;
            printStream.print(CLEAR_SCREEN);
            printStream.print(MOVE_CURSOR_HOME);
            printStream.print(frame);
            if (!frame.endsWith(System.lineSeparator())) {
                printStream.println();
            }
            printStream.flush();
            Thread.sleep(delayMillis);
        } catch (IOException e) {
            // ASCII 출력은 부가 기능이므로 리소스를 읽지 못해도 전투 흐름은 유지
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (cursorHidden) {
                printStream.print(SHOW_CURSOR);
                printStream.flush();
            }
        }
    }

    private String readFrame(final String action) throws IOException {
        String resourcePath = "static/" + action + ".txt";
        try (InputStream inputStream = AnimationPlayer.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        return Files.readString(STATIC_RESOURCE_PATH.resolve(action + ".txt"));
    }
}
