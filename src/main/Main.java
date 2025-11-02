package main;

import controller.GameEngine;
import model.City;
import view.GameWindow;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                City model = new City();

                GameEngine controller = new GameEngine(model);

                GameWindow view = new GameWindow(controller);

                controller.setView(view);

                controller.startGame();
            }
        });
    }
}
