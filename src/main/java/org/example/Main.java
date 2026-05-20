package org.example;

import org.example.controller.GameController;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

public class Main {
    public static void main(String[] args) {
        final GameController controller = new GameController(
                new InputView(),
                new OutputView()
        );
        controller.run();
    }
}
