package org.example.view.in;

import java.util.Objects;
import java.util.Scanner;

public class InputView {
    private final Scanner scanner;
    private final boolean finiteInput;

    public InputView() {
        this(new Scanner(System.in), false);
    }

    public InputView(final Scanner scanner) {
        this(scanner, true);
    }

    private InputView(final Scanner scanner, final boolean finiteInput) {
        this.scanner = Objects.requireNonNull(scanner);
        this.finiteInput = finiteInput;
    }

    public int inputNumber() {
        try {
            return Integer.parseInt(readLine());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("숫자를 입력해야 합니다.", e);
        }
    }

    public boolean canRetry() {
        return !finiteInput || scanner.hasNextLine();
    }

    private String readLine() {
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("입력이 없습니다.");
        }

        return scanner.nextLine();
    }
}
