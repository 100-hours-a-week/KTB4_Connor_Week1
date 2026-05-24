package org.example;

import org.example.controller.GameController;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

public class Main {
    public static void main(String[] args) {
        try (OutputView outputView = new OutputView()) {
            final GameController controller = new GameController(
                    new InputView(),
                    outputView
            );
            controller.run();
        }
    }
}
