package org.example.view.out;

import java.io.PrintStream;

public class OutputView {
    private PrintStream out;

    public OutputView(PrintStream out) {
        this.out = out;
    }

    public void print(String message) {
        out.println(message);
    }
}
