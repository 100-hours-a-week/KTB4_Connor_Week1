package org.example;

import org.example.controller.GameController;
import org.example.view.in.InputView;
import org.example.view.out.OutputView;

import java.io.PrintStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InputView inputView = new InputView(new Scanner(System.in));
        OutputView outputView = new OutputView(new PrintStream(System.out));
        GameController controller = new GameController(
                inputView,
                outputView
        );

        controller.run();
    }
}