package org.example.view.out;

import java.io.PrintStream;
import java.util.Objects;

public class OutputView {
    private final PrintStream printStream;

    public OutputView() {
        this(System.out);
    }

    public OutputView(final PrintStream printStream) {
        this.printStream = Objects.requireNonNull(printStream);
    }

    public void print(String message) {
        printStream.println(message);
    }
}
